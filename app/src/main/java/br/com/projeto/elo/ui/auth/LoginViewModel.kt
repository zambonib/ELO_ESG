package br.com.projeto.elo.ui.auth

import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor() : ViewModel() {

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    private val _email = MutableStateFlow("")
    val email: StateFlow<String> = _email.asStateFlow()

    private val _senha = MutableStateFlow("")
    val senha: StateFlow<String> = _senha.asStateFlow()

    private val _mensagemErro = MutableStateFlow("")
    val mensagemErro: StateFlow<String> = _mensagemErro.asStateFlow()

    fun atualizarEmail(novoEmail: String) { _email.value = novoEmail }

    fun atualizarSenha(novaSenha: String) { _senha.value = novaSenha }

    fun realizarLogin(aoSucesso: () -> Unit) {
        if (_email.value.isEmpty() || _senha.value.isEmpty()) {
            _mensagemErro.value = "Preencha e-mail e senha!"
            return
        }
        auth.signInWithEmailAndPassword(_email.value, _senha.value)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    _mensagemErro.value = ""
                    aoSucesso()
                } else {
                    _mensagemErro.value = "E-mail ou senha incorretos!"
                }
            }
    }
}