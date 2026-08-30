package br.com.projeto.elo.ui.dashboard

import android.app.Application
import android.content.SharedPreferences
import android.net.Uri
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import br.com.projeto.elo.BuildConfig
import br.com.projeto.elo.data.local.TransacaoDao
import br.com.projeto.elo.data.remote.Content
import br.com.projeto.elo.data.remote.GeminiApi
import br.com.projeto.elo.data.remote.GeminiRequest
import br.com.projeto.elo.data.remote.Part
import br.com.projeto.elo.dominio.modelo.Transacao
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.util.Calendar
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(
    application: Application,
    private val transacaoDao: TransacaoDao,
    private val geminiApi: GeminiApi,
    private val prefs: SharedPreferences
) : AndroidViewModel(application) {

    private val auth = FirebaseAuth.getInstance()
    private val uid = auth.currentUser?.uid ?: ""

    private val _textoBusca = MutableStateFlow("")
    val textoBusca: StateFlow<String> = _textoBusca.asStateFlow()

    private val _transacoes = MutableStateFlow<List<Transacao>>(emptyList())
    val transacoes: StateFlow<List<Transacao>> = _transacoes.asStateFlow()

    private val _receitaDoMes = MutableStateFlow(0.0)
    val receitaDoMes: StateFlow<Double> = _receitaDoMes.asStateFlow()

    private val _despesaDoMes = MutableStateFlow(0.0)
    val despesaDoMes: StateFlow<Double> = _despesaDoMes.asStateFlow()

    private val _carregandoIa = MutableStateFlow(false)
    val carregandoIa: StateFlow<Boolean> = _carregandoIa.asStateFlow()

    private val _fotoUri = MutableStateFlow<Uri?>(
        prefs.getString("foto_$uid", null)?.let { Uri.parse(it) }
    )
    val fotoUri: StateFlow<Uri?> = _fotoUri.asStateFlow()

    private fun calcularNome(): String {
        val nomeSalvo = prefs.getString("nome_$uid", null)
        if (!nomeSalvo.isNullOrBlank()) return nomeSalvo
        val email = auth.currentUser?.email ?: ""
        return when (email.lowercase()) {
            "cliente@elo.com.br"  -> "Maria"
            "cliente1@elo.com.br" -> "Marcelo"
            else -> auth.currentUser?.displayName?.ifBlank { null }
                ?: email.substringBefore("@")
        }
    }

    private val _nomeUsuario = MutableStateFlow(calcularNome())
    val nomeUsuario: StateFlow<String> = _nomeUsuario.asStateFlow()

    private val inicioDoMes: Long
        get() {
            val cal = Calendar.getInstance()
            cal.set(Calendar.DAY_OF_MONTH, 1)
            cal.set(Calendar.HOUR_OF_DAY, 0)
            cal.set(Calendar.MINUTE, 0)
            cal.set(Calendar.SECOND, 0)
            cal.set(Calendar.MILLISECOND, 0)
            return cal.timeInMillis
        }

    init {
        viewModelScope.launch {
            _textoBusca.debounce(300).collectLatest { busca ->
                val flow = if (busca.isBlank()) transacaoDao.obterTodasTransacoes(uid)
                else transacaoDao.buscarTransacoes(uid, busca)
                flow.collect { _transacoes.value = it }
            }
        }
        viewModelScope.launch {
            transacaoDao.obterReceitaDoMes(uid, inicioDoMes).collect { _receitaDoMes.value = it }
        }
        viewModelScope.launch {
            transacaoDao.obterDespesaDoMes(uid, inicioDoMes).collect { _despesaDoMes.value = it }
        }
    }

    fun atualizarBusca(texto: String) { _textoBusca.value = texto }

    fun salvarFoto(uri: Uri) {
        prefs.edit().putString("foto_$uid", uri.toString()).apply()
        _fotoUri.value = uri
    }

    fun sairDoAplicativo(aoSair: () -> Unit) { auth.signOut(); aoSair() }

    fun excluirTransacao(transacao: Transacao) {
        viewModelScope.launch { transacaoDao.excluirTransacao(transacao) }
    }

    fun editarTransacao(transacao: Transacao) {
        viewModelScope.launch { transacaoDao.atualizarTransacao(transacao) }
    }

    fun trocarNome(novoNome: String, aoTerminar: (Boolean) -> Unit) {
        viewModelScope.launch {
            try {
                prefs.edit().putString("nome_$uid", novoNome).apply()
                val request = UserProfileChangeRequest.Builder().setDisplayName(novoNome).build()
                auth.currentUser?.updateProfile(request)?.await()
                _nomeUsuario.value = novoNome
                aoTerminar(true)
            } catch (e: Exception) {
                _nomeUsuario.value = novoNome
                aoTerminar(true)
            }
        }
    }

    fun trocarSenha(senhaAtual: String, novaSenha: String, aoTerminar: (Boolean, String) -> Unit) {
        viewModelScope.launch {
            try {
                val email = auth.currentUser?.email ?: ""
                val credencial = EmailAuthProvider.getCredential(email, senhaAtual)
                auth.currentUser?.reauthenticate(credencial)?.await()
                auth.currentUser?.updatePassword(novaSenha)?.await()
                aoTerminar(true, "Senha alterada com sucesso!")
            } catch (e: Exception) {
                aoTerminar(false, "Erro: ${e.message}")
            }
        }
    }

    fun registrarGastoComInteligencia(textoDoUsuario: String) {
        viewModelScope.launch {
            _carregandoIa.value = true
            try {
                val instrucao = """
                    Você é o cérebro de um app financeiro. O usuário enviou: "$textoDoUsuario"
                    Analise se é uma entrada de dinheiro (RECEITA) ou saída (DESPESA).
                    Extraia o valor numérico, gere uma descrição curta e defina a categoria.
                    Retorne APENAS um JSON válido, sem texto extra.
                    Exemplo 1: {"descricao":"Mercado","valor":50.0,"categoria":"Alimentação","tipo":"DESPESA"}
                    Exemplo 2: {"descricao":"Salário","valor":2000.0,"categoria":"Renda","tipo":"RECEITA"}
                """.trimIndent()
                val request = GeminiRequest(listOf(Content(listOf(Part(instrucao)))))
                val resposta = geminiApi.classificarTransacao(BuildConfig.GEMINI_API_KEY, request)
                val textoResposta = resposta.candidates?.firstOrNull()?.content?.parts?.firstOrNull()?.text
                if (textoResposta != null) {
                    val jsonLimpo = textoResposta.replace("```json", "").replace("```", "").trim()
                    val transacaoSemUsuario = Gson().fromJson(jsonLimpo, Transacao::class.java)
                    transacaoDao.inserirTransacao(
                        transacaoSemUsuario.copy(usuarioId = uid, data = System.currentTimeMillis())
                    )
                    Toast.makeText(getApplication(), "✅ Lançamento salvo!", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Toast.makeText(getApplication(), "Erro na IA: ${e.message}", Toast.LENGTH_LONG).show()
            } finally {
                _carregandoIa.value = false
            }
        }
    }
}