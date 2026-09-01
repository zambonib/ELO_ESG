
package br.com.projeto.elo.ui.screens

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import br.com.projeto.elo.BuildConfig
import br.com.projeto.elo.data.remote.Content
import br.com.projeto.elo.data.remote.GeminiApi
import br.com.projeto.elo.data.remote.GeminiRequest
import br.com.projeto.elo.data.remote.Part
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException
import javax.inject.Inject

@HiltViewModel
class EconomizeViewModel @Inject constructor(
    application: Application,
    private val geminiApi: GeminiApi
) : AndroidViewModel(application) {

    private val _queryCalculadora = MutableStateFlow("")
    val queryCalculadora: StateFlow<String> = _queryCalculadora.asStateFlow()

    private val _respostaCalculadora = MutableStateFlow<String?>(null)
    val respostaCalculadora: StateFlow<String?> = _respostaCalculadora.asStateFlow()

    private val _carregandoCalculadora = MutableStateFlow(false)
    val carregandoCalculadora: StateFlow<Boolean> = _carregandoCalculadora.asStateFlow()

    fun atualizarQueryCalculadora(novaQuery: String) {
        _queryCalculadora.value = novaQuery
    }

    fun calcularEconomiaAi(pergunta: String) {
        if (pergunta.isBlank()) return

        viewModelScope.launch {
            _carregandoCalculadora.value = true
            _respostaCalculadora.value = null

            try {
                val instrucao = """
                    Você é um especialista em eficiência energética do aplicativo financeiro Elo.
                    Sua tarefa é estimar a economia de energia em kWh e/ou Reais baseada na ação descrita pelo usuário.

                    REGRA 1: Se a pergunta NÃO for estritamente sobre economia de energia, água, sustentabilidade ou eficiência de aparelhos elétricos/eletrônicos, você DEVE responder EXATAMENTE com a seguinte frase e nada mais:
                    "Por favor, me faça perguntas relacionadas apenas à economia de energia, água ou sustentabilidade. Meu objetivo aqui é calcular seus ganhos sustentáveis!"

                    REGRA 2: Se for sobre energia/água, faça uma estimativa realista baseada em médias de mercado brasileiras.
                    REGRA 3: Seja breve, direto ao ponto e use negrito (**) para destacar os valores economizados. Não passe de um parágrafo.

                    Pergunta do usuário: $pergunta
                """.trimIndent()

                val request = GeminiRequest(listOf(Content(listOf(Part(instrucao)))))
                val resposta = geminiApi.classificarTransacao(BuildConfig.GEMINI_API_KEY, request)
                val texto = resposta.candidates?.firstOrNull()?.content?.parts?.firstOrNull()?.text

                if (texto != null) {
                    _respostaCalculadora.value = texto.trim()
                } else {
                    _respostaCalculadora.value = "Não consegui processar essa pergunta agora. Tente novamente."
                }

            } catch (e: HttpException) {
                e.printStackTrace()
                val msgAmigavel = when (e.code()) {
                    403 -> "Acesso negado à IA. Verifique se a chave colada está correta e ativa."
                    503 -> "Servidor da IA temporariamente sobrecarregado. Tente novamente em alguns segundos."
                    404 -> "Modelo da IA não encontrado. Verifique a conexão."
                    else -> "Desculpe, ocorreu um erro de conexão com a IA (HTTP ${e.code()})."
                }
                _respostaCalculadora.value = msgAmigavel

            } catch (e: Exception) {
                e.printStackTrace()
                _respostaCalculadora.value = "Desculpe, ocorreu um erro genérico ao calcular. Tente novamente."

            } finally {
                _carregandoCalculadora.value = false
            }
        }
    }
}
