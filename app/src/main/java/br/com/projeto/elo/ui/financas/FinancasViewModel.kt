package br.com.projeto.elo.ui.financas

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import br.com.projeto.elo.BuildConfig
import br.com.projeto.elo.data.local.OrcamentoDao
import br.com.projeto.elo.data.local.TransacaoDao
import br.com.projeto.elo.data.remote.Content
import br.com.projeto.elo.data.remote.GeminiApi
import br.com.projeto.elo.data.remote.GeminiRequest
import br.com.projeto.elo.data.remote.Part
import br.com.projeto.elo.dominio.modelo.CategoriaTotal
import br.com.projeto.elo.dominio.modelo.OrcamentoCategoria
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.Calendar
import javax.inject.Inject

// Resumo de um mês para o gráfico de evolução
data class MesResumo(val anchor: Long, val receitas: Double, val despesas: Double)

// Orçamento de uma categoria combinado com o quanto já foi gasto no mês
data class OrcamentoComGasto(val categoria: String, val limite: Double, val gasto: Double) {
    val progresso: Float get() = if (limite > 0) (gasto / limite).toFloat() else 0f
    val estourou: Boolean get() = gasto > limite
}

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class FinancasViewModel @Inject constructor(
    application: Application,
    private val transacaoDao: TransacaoDao,
    private val orcamentoDao: OrcamentoDao,
    private val geminiApi: GeminiApi
) : AndroidViewModel(application) {

    private val auth = FirebaseAuth.getInstance()
    private val uid = auth.currentUser?.uid ?: ""

    // Âncora = primeiro milissegundo do mês selecionado
    private val _mesAnchor = MutableStateFlow(inicioDoMes(Calendar.getInstance()))
    val mesAnchor: StateFlow<Long> = _mesAnchor.asStateFlow()

    // ── Somatórios do mês selecionado ────────────────────────────────
    val receitas: StateFlow<Double> = _mesAnchor
        .flatMapLatest { anchor ->
            val (inicio, fim) = periodo(anchor)
            transacaoDao.obterReceitaPorPeriodo(uid, inicio, fim)
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0.0)

    val despesas: StateFlow<Double> = _mesAnchor
        .flatMapLatest { anchor ->
            val (inicio, fim) = periodo(anchor)
            transacaoDao.obterDespesaPorPeriodo(uid, inicio, fim)
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0.0)

    val saldo: StateFlow<Double> = combine(receitas, despesas) { r, d -> r - d }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0.0)

    val taxaPoupanca: StateFlow<Float> = combine(receitas, despesas) { r, d ->
        calcularTaxaPoupanca(r, d)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0f)

    // ── Gastos por categoria (apenas despesas) ───────────────────────
    val gastosPorCategoria: StateFlow<List<CategoriaTotal>> = _mesAnchor
        .flatMapLatest { anchor ->
            val (inicio, fim) = periodo(anchor)
            transacaoDao.obterGastosPorCategoria(uid, inicio, fim)
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // ── Orçamentos combinados com o gasto atual de cada categoria ────
    val orcamentos: StateFlow<List<OrcamentoComGasto>> =
        combine(orcamentoDao.obterOrcamentos(uid), gastosPorCategoria) { limites, gastos ->
            val mapaGastos = gastos.associate { it.categoria to it.total }
            limites
                .map { OrcamentoComGasto(it.categoria, it.limite, mapaGastos[it.categoria] ?: 0.0) }
                .sortedByDescending { it.progresso }
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // ── Score de saúde financeira (0..100) e rótulo ──────────────────
    val scoreSaude: StateFlow<Int> = combine(taxaPoupanca, orcamentos) { taxa, orcs ->
        calcularScore(taxa, aderenciaOrcamento(orcs))
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    // ── Evolução dos últimos 6 meses a partir do mês selecionado ─────
    val evolucao6Meses: StateFlow<List<MesResumo>> = _mesAnchor
        .flatMapLatest { anchor ->
            val anchors = (5 downTo 0).map { deslocarMes(anchor, -it) }
            val flows = anchors.map { a ->
                val (inicio, fim) = periodo(a)
                combine(
                    transacaoDao.obterReceitaPorPeriodo(uid, inicio, fim),
                    transacaoDao.obterDespesaPorPeriodo(uid, inicio, fim)
                ) { r, d -> MesResumo(a, r, d) }
            }
            combine(flows) { it.toList() }
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // ── Dica de educação financeira gerada pela IA (sob demanda) ─────
    private val _dicaIa = MutableStateFlow<String?>(null)
    val dicaIa: StateFlow<String?> = _dicaIa.asStateFlow()

    private val _carregandoDica = MutableStateFlow(false)
    val carregandoDica: StateFlow<Boolean> = _carregandoDica.asStateFlow()

    // ── Ações ────────────────────────────────────────────────────────
    fun voltarMes() {
        _mesAnchor.value = deslocarMes(_mesAnchor.value, -1)
    }

    fun avancarMes() {
        _mesAnchor.value = deslocarMes(_mesAnchor.value, 1)
    }

    fun definirLimite(categoria: String, valor: Double) {
        if (categoria.isBlank() || valor <= 0) return
        viewModelScope.launch {
            orcamentoDao.definirOrcamento(
                OrcamentoCategoria(usuarioId = uid, categoria = categoria.trim(), limite = valor)
            )
        }
    }

    fun removerLimite(categoria: String) {
        viewModelScope.launch { orcamentoDao.removerOrcamento(uid, categoria) }
    }

    fun gerarDicaFinanceira() {
        if (_carregandoDica.value) return
        viewModelScope.launch {
            _carregandoDica.value = true
            try {
                val r = receitas.value
                val d = despesas.value
                val taxa = (taxaPoupanca.value * 100).toInt()
                val topCategorias = gastosPorCategoria.value.take(3)
                    .joinToString("; ") { "${it.categoria}: R$ ${"%.2f".format(it.total)}" }
                    .ifBlank { "sem despesas registradas" }

                val instrucao = """
                    Você é um educador financeiro do app ELO, voltado a pessoas de baixa renda no Brasil.
                    Dados do mês do usuário:
                    - Receitas: R$ ${"%.2f".format(r)}
                    - Despesas: R$ ${"%.2f".format(d)}
                    - Taxa de poupança: $taxa%
                    - Maiores gastos por categoria: $topCategorias
                    Gere UMA dica curta (no máximo 2 frases), prática, acolhedora e em português do Brasil,
                    para melhorar a saúde financeira. Responda apenas com a dica, sem markdown e sem aspas.
                """.trimIndent()

                val request = GeminiRequest(listOf(Content(listOf(Part(instrucao)))))
                val resposta = geminiApi.classificarTransacao(BuildConfig.GEMINI_API_KEY, request)
                val texto = resposta.candidates?.firstOrNull()?.content?.parts?.firstOrNull()?.text
                _dicaIa.value = texto
                    ?.replace("```", "")
                    ?.trim()
                    ?.ifBlank { "Não consegui gerar uma dica agora. Tente novamente." }
                    ?: "Não consegui gerar uma dica agora. Tente novamente."
            } catch (e: Exception) {
                _dicaIa.value = "Erro ao gerar a dica: ${e.message}"
            } finally {
                _carregandoDica.value = false
            }
        }
    }

    // ── Utilidades de data ───────────────────────────────────────────
    private fun inicioDoMes(cal: Calendar): Long {
        cal.set(Calendar.DAY_OF_MONTH, 1)
        cal.set(Calendar.HOUR_OF_DAY, 0)
        cal.set(Calendar.MINUTE, 0)
        cal.set(Calendar.SECOND, 0)
        cal.set(Calendar.MILLISECOND, 0)
        return cal.timeInMillis
    }

    private fun deslocarMes(anchor: Long, meses: Int): Long {
        val cal = Calendar.getInstance().apply { timeInMillis = anchor }
        cal.add(Calendar.MONTH, meses)
        return inicioDoMes(cal)
    }

    // Retorna o par (início, fim) em milissegundos do mês cuja âncora foi informada
    private fun periodo(anchor: Long): Pair<Long, Long> {
        val cal = Calendar.getInstance().apply { timeInMillis = anchor }
        cal.add(Calendar.MONTH, 1)
        return anchor to (cal.timeInMillis - 1)
    }

    companion object {
        // Taxa de poupança = (receitas - despesas) / receitas. Zero quando não há receita.
        fun calcularTaxaPoupanca(receitas: Double, despesas: Double): Float {
            if (receitas <= 0.0) return 0f
            return ((receitas - despesas) / receitas).toFloat()
        }

        // Fração de orçamentos respeitados (0..1); neutro (1) quando não há orçamentos definidos.
        fun aderenciaOrcamento(orcamentos: List<OrcamentoComGasto>): Float {
            if (orcamentos.isEmpty()) return 1f
            val respeitados = orcamentos.count { !it.estourou }
            return respeitados.toFloat() / orcamentos.size
        }

        // Score 0..100: 70% taxa de poupança + 30% aderência ao orçamento.
        fun calcularScore(taxaPoupanca: Float, aderenciaOrcamento: Float): Int {
            val poup = taxaPoupanca.coerceIn(0f, 1f)
            val ader = aderenciaOrcamento.coerceIn(0f, 1f)
            return ((poup * 0.7f + ader * 0.3f) * 100).toInt().coerceIn(0, 100)
        }
    }
}
