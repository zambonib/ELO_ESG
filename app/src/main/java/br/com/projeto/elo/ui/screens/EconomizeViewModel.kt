package br.com.projeto.elo.ui.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel para a tela Economize.
 * Gerencia o estado da calculadora de economia baseada em IA (Gemini).
 */
@HiltViewModel
class EconomizeViewModel @Inject constructor() : ViewModel() {

    // --- ESTADOS DA CALCULADORA DE ECONOMIA (IA) ---

    // Armazena a pergunta/query digitada pelo usuário no input
    private val _queryCalculadora = MutableStateFlow("")
    val queryCalculadora: StateFlow<String> = _queryCalculadora.asStateFlow()

    // Armazena a resposta recebida do Gemini (ou null se não houver)
    private val _respostaCalculadora = MutableStateFlow<String?>(null)
    val respostaCalculadora: StateFlow<String?> = _respostaCalculadora.asStateFlow()

    // Controla o estado de carregamento (mostra o spinner no botão)
    private val _carregandoCalculadora = MutableStateFlow(false)
    val carregandoCalculadora: StateFlow<Boolean> = _carregandoCalculadora.asStateFlow()

    // --- FUNÇÕES DE INTERAÇÃO COM A UI ---

    /**
     * Atualiza o estado da query conforme o usuário digita.
     */
    fun atualizarQueryCalculadora(novaQuery: String) {
        _queryCalculadora.value = novaQuery
    }

    /**
     * Envia a pergunta do usuário para a IA calcular a economia estimada.
     * Atualmente simulado com delay e resposta fixa.
     */
    fun calcularEconomiaAi(pergunta: String) {
        if (pergunta.isBlank()) return

        viewModelScope.launch {
            // Inicia o estado de carregamento e limpa resposta anterior
            _carregandoCalculadora.value = true
            _respostaCalculadora.value = null

            // -----------------------------------------------------------------------------------
            // TODO: INTEGRAR CHAMADA REAL PARA O GEMINI AQUI
            //
            // Exemplo de como seria com um repositório:
            // try {
            //     val resultado = geminiRepository.perguntarCalculoEconomia(pergunta)
            //     _respostaCalculadora.value = resultado
            // } catch (e: Exception) {
            //     _respostaCalculadora.value = "Desculpe, ocorreu um erro ao calcular. Tente novamente."
            // }
            // -----------------------------------------------------------------------------------

            // --- SIMULAÇÃO (Remova isto quando integrar o Gemini real) ---
            // Simula um delay de rede de 2.5 segundos
            delay(2500)

            // Resposta de exemplo baseada na pergunta comum de lâmpadas
            _respostaCalculadora.value = """
                Com base em médias de mercado, trocar 10 lâmpadas incandescentes comuns por LED geraria uma economia estimada de aproximadamente **25 kWh por mês**.

                Isso representa uma redução de cerca de **R$ 22,50** na sua conta (considerando uma tarifa média de R$ 0,90 por kWh). Essa estimativa supõe um uso médio de 5 horas por dia.
            """.trimIndent()
            // -----------------------------------------------------------------

            // Finaliza o estado de carregamento
            _carregandoCalculadora.value = false
        }
    }
}
