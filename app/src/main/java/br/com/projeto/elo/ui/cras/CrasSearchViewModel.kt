package br.com.projeto.elo.ui.cras

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.projeto.elo.data.remote.ViaCepApi
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

data class MockCras(
    val id: String,
    val nome: String,
    val endereco: String,
    val bairro: String,
    val distancia: String,
    val latitude: Double? = null,
    val longitude: Double? = null
)

@HiltViewModel
class CrasSearchViewModel @Inject constructor(
    private val viaCepApi: ViaCepApi,
    private val firestore: FirebaseFirestore
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
                // 1. Busca no ViaCEP
                val response = viaCepApi.buscarCep(cep)
                
                if (response.erro == true || response.logradouro == null) {
                    _erro.value = "CEP não encontrado ou inválido."
                } else {
                    _enderecoEncontrado.value = "${response.logradouro} - ${response.bairro}, ${response.localidade}/${response.uf}"
                    
                    // 2. Com o IBGE do ViaCEP (7 dígitos), pegamos os 6 primeiros para bater com o Firebase
                    val ibgeViaCep = response.ibge
                    if (ibgeViaCep != null && ibgeViaCep.length >= 6) {
                        val ibgeFirebase = ibgeViaCep.substring(0, 6)
                        
                        // 3. Query no Firestore
                        val snapshot = firestore.collection("cras")
                            .whereEqualTo("ibge", ibgeFirebase)
                            .get()
                            .await()
                            
                        if (snapshot.isEmpty) {
                            _erro.value = "Nenhum CRAS encontrado nesta cidade."
                        } else {
                            val listaCras = snapshot.documents.map { doc ->
                                val nome = doc.getString("nome") ?: "CRAS"
                                val endereco = doc.getString("endereco") ?: ""
                                val numero = doc.getString("numero") ?: ""
                                val bairro = doc.getString("bairro") ?: ""
                                val lat = doc.getDouble("latitude")
                                val lng = doc.getDouble("longitude")
                                
                                MockCras(
                                    id = doc.id,
                                    nome = nome,
                                    endereco = if (numero.isNotEmpty()) "$endereco, $numero" else endereco,
                                    bairro = bairro,
                                    distancia = "Sua cidade",
                                    latitude = lat,
                                    longitude = lng
                                )
                            }
                            _resultados.value = listaCras
                        }
                    } else {
                        _erro.value = "Não foi possível obter o código IBGE para buscar os CRAS."
                    }
                }
            } catch (e: Exception) {
                _erro.value = "Erro de conexão. Tente novamente."
                e.printStackTrace()
            } finally {
                _carregando.value = false
            }
        }
    }
}
