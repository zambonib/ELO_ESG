package br.com.projeto.elo.ui.cras

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.projeto.elo.data.remote.ViaCepApi
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class MockCras(
    val id: Int,
    val nome: String,
    val endereco: String,
    val bairro: String,
    val distancia: String
)

@HiltViewModel
class CrasSearchViewModel @Inject constructor(
    private val viaCepApi: ViaCepApi
) : ViewModel() {

    private val _cepDigitado = MutableStateFlow("")
    val cepDigitado: StateFlow<String> = _cepDigitado.asStateFlow()

    private val _carregando = MutableStateFlow(false)
    val carregando: StateFlow<Boolean> = _carregando.asStateFlow()

    private val _erro = MutableStateFlow<String?>(null)
    val erro: StateFlow<String?> = _erro.asStateFlow()

    private val _resultados = MutableStateFlow<List<MockCras>>(emptyList())
    val resultados: StateFlow<List<MockCras>> = _resultados.asStateFlow()

    private val _enderecoEncontrado = MutableStateFlow<String?>(null)
    val enderecoEncontrado: StateFlow<String?> = _enderecoEncontrado.asStateFlow()

    fun onCepChanged(cep: String) {
        val soNumeros = cep.filter { it.isDigit() }
        if (soNumeros.length <= 8) {
            _cepDigitado.value = soNumeros
        }
    }

    fun buscarCras() {
        val cep = _cepDigitado.value
        if (cep.length != 8) {
            _erro.value = "Por favor, digite um CEP válido com 8 números."
            return
        }

        _carregando.value = true
        _erro.value = null
        _resultados.value = emptyList()
        _enderecoEncontrado.value = null

        viewModelScope.launch {
            try {
                val response = viaCepApi.buscarCep(cep)
                if (response.erro == true || response.logradouro == null) {
                    _erro.value = "CEP não encontrado ou inválido."
                } else {
                    _enderecoEncontrado.value = "${response.logradouro} - ${response.bairro}, ${response.localidade}/${response.uf}"
                    _resultados.value = listOf(
                        MockCras(
                            id = 1,
                            nome = "CRAS Centro - Unidade I",
                            endereco = "Rua das Flores, 123",
                            bairro = response.bairro ?: "Centro",
                            distancia = "Aprox. 1.2 km"
                        ),
                        MockCras(
                            id = 2,
                            nome = "CRAS Esperança - Unidade II",
                            endereco = "Av. Brasil, 450",
                            bairro = response.bairro ?: "Bairro Esperança",
                            distancia = "Aprox. 3.5 km"
                        )
                    )
                }
            } catch (e: Exception) {
                _erro.value = "Erro de conexão ao buscar o CEP. Tente novamente."
                e.printStackTrace()
            } finally {
                _carregando.value = false
            }
        }
    }
}
