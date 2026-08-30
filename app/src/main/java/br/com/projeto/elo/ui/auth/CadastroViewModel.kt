package br.com.projeto.elo.ui.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

@HiltViewModel
class CadastroViewModel @Inject constructor() : ViewModel() {

    private val auth = FirebaseAuth.getInstance()

    private val _nome = MutableStateFlow("")
    val nome: StateFlow<String> = _nome.asStateFlow()

    private val _email = MutableStateFlow("")
    val email: StateFlow<String> = _email.asStateFlow()

    private val _senha = MutableStateFlow("")
    val senha: StateFlow<String> = _senha.asStateFlow()

    private val _confirmarSenha = MutableStateFlow("")
    val confirmarSenha: StateFlow<String> = _confirmarSenha.asStateFlow()

    private val _mensagemErro = MutableStateFlow("")
    val mensagemErro: StateFlow<String> = _mensagemErro.asStateFlow()

    private val _carregando = MutableStateFlow(false)
    val carregando: StateFlow<Boolean> = _carregando.asStateFlow()

    fun atualizarNome(v: String) { _nome.value = v }
    fun atualizarEmail(v: String) { _email.value = v }
    fun atualizarSenha(v: String) { _senha.value = v }
    fun atualizarConfirmarSenha(v: String) { _confirmarSenha.value = v }

    fun formularioValido(): Boolean =
        _nome.value.isNotBlank() &&
                _email.value.contains("@") &&
                _senha.value.length >= 6 &&
                _senha.value == _confirmarSenha.value

    fun criarConta(aoSucesso: () -> Unit) {
        if (!formularioValido()) {
            _mensagemErro.value = when {
                _nome.value.isBlank() -> "Informe seu nome completo."
                !_email.value.contains("@") -> "E-mail inválido."
                _senha.value.length < 6 -> "A senha deve ter no mínimo 6 caracteres."
                _senha.value != _confirmarSenha.value -> "As senhas não coincidem."
                else -> "Verifique os campos."
            }
            return
        }
        viewModelScope.launch {
            _carregando.value = true
            _mensagemErro.value = ""
            try {
                auth.createUserWithEmailAndPassword(_email.value, _senha.value).await()
                // Salva o nome no perfil do Firebase
                val request = UserProfileChangeRequest.Builder()
                    .setDisplayName(_nome.value).build()
                auth.currentUser?.updateProfile(request)?.await()
                aoSucesso()
            } catch (e: Exception) {
                _mensagemErro.value = when {
                    e.message?.contains("email address is already in use") == true ->
                        "Este e-mail já está cadastrado."
                    e.message?.contains("badly formatted") == true ->
                        "Formato de e-mail inválido."
                    else -> "Erro ao criar conta: ${e.message}"
                }
            } finally {
                _carregando.value = false
            }
        }
    }
}