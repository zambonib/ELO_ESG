package br.com.projeto.elo.ui.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import br.com.projeto.elo.ui.theme.LaranjaBotao
import br.com.projeto.elo.ui.theme.VerdeCard
import br.com.projeto.elo.ui.theme.VerdeFundo
import br.com.projeto.elo.ui.theme.VermelhoSeta

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CadastroTela(
    viewModel: CadastroViewModel = viewModel(),
    aoContaCriada: () -> Unit = {},
    aoVoltar: () -> Unit = {}
) {
    val nome by viewModel.nome.collectAsState()
    val email by viewModel.email.collectAsState()
    val senha by viewModel.senha.collectAsState()
    val confirmarSenha by viewModel.confirmarSenha.collectAsState()
    val mensagemErro by viewModel.mensagemErro.collectAsState()
    val carregando by viewModel.carregando.collectAsState()

    var senhaVisivel by remember { mutableStateOf(false) }
    var confirmarSenhaVisivel by remember { mutableStateOf(false) }
    val focusManager = LocalFocusManager.current

    val senhasCoincidindo = confirmarSenha.isEmpty() || senha == confirmarSenha
    val senhaForte = senha.isEmpty() || senha.length >= 6

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Criar Conta", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = aoVoltar) {
                        Icon(Icons.Default.ArrowBack, "Voltar")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = VerdeFundo,
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(VerdeFundo)
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
        ) {
            // Cabeçalho
            Column(
                modifier = Modifier.fillMaxWidth().padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("🌱", fontSize = 48.sp)
                Spacer(Modifier.height(8.dp))
                Text("Bem-vindo ao ELO!", color = Color.White,
                    fontSize = 22.sp, fontWeight = FontWeight.Bold)
                Text("Crie sua conta gratuita", color = Color.White.copy(alpha = 0.8f), fontSize = 15.sp)
            }

            // Card do formulário
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth().padding(28.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    Text("Seus dados", fontSize = 20.sp, fontWeight = FontWeight.Bold)

                    // Nome completo
                    OutlinedTextField(
                        value = nome,
                        onValueChange = { viewModel.atualizarNome(it) },
                        label = { Text("Nome completo") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                        keyboardActions = KeyboardActions(onNext = { focusManager.moveFocus(FocusDirection.Down) }),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = VerdeFundo,
                            unfocusedBorderColor = Color.LightGray
                        ),
                        singleLine = true
                    )

                    // E-mail
                    OutlinedTextField(
                        value = email,
                        onValueChange = { viewModel.atualizarEmail(it) },
                        label = { Text("E-mail") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Email,
                            imeAction = ImeAction.Next
                        ),
                        keyboardActions = KeyboardActions(onNext = { focusManager.moveFocus(FocusDirection.Down) }),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = VerdeFundo,
                            unfocusedBorderColor = Color.LightGray
                        ),
                        singleLine = true
                    )

                    // Senha
                    OutlinedTextField(
                        value = senha,
                        onValueChange = { viewModel.atualizarSenha(it) },
                        label = { Text("Senha (mín. 6 caracteres)") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        visualTransformation = if (senhaVisivel) VisualTransformation.None
                        else PasswordVisualTransformation(),
                        trailingIcon = {
                            IconButton(onClick = { senhaVisivel = !senhaVisivel }) {
                                Icon(if (senhaVisivel) Icons.Default.VisibilityOff
                                else Icons.Default.Visibility, null)
                            }
                        },
                        isError = !senhaForte,
                        supportingText = if (!senhaForte) {{ Text("Mínimo 6 caracteres", color = VermelhoSeta) }} else null,
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Password,
                            imeAction = ImeAction.Next
                        ),
                        keyboardActions = KeyboardActions(onNext = { focusManager.moveFocus(FocusDirection.Down) }),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = VerdeFundo,
                            unfocusedBorderColor = Color.LightGray
                        ),
                        singleLine = true
                    )

                    // Confirmar senha
                    OutlinedTextField(
                        value = confirmarSenha,
                        onValueChange = { viewModel.atualizarConfirmarSenha(it) },
                        label = { Text("Confirmar senha") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        visualTransformation = if (confirmarSenhaVisivel) VisualTransformation.None
                        else PasswordVisualTransformation(),
                        trailingIcon = {
                            IconButton(onClick = { confirmarSenhaVisivel = !confirmarSenhaVisivel }) {
                                Icon(if (confirmarSenhaVisivel) Icons.Default.VisibilityOff
                                else Icons.Default.Visibility, null)
                            }
                        },
                        isError = !senhasCoincidindo,
                        supportingText = if (!senhasCoincidindo) {{ Text("As senhas não coincidem", color = VermelhoSeta) }} else null,
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Password,
                            imeAction = ImeAction.Done
                        ),
                        keyboardActions = KeyboardActions(
                            onDone = {
                                focusManager.clearFocus()
                                if (viewModel.formularioValido()) viewModel.criarConta(aoContaCriada)
                            }
                        ),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = VerdeFundo,
                            unfocusedBorderColor = Color.LightGray
                        ),
                        singleLine = true
                    )

                    // Mensagem de erro do servidor
                    if (mensagemErro.isNotEmpty()) {
                        Card(
                            colors = CardDefaults.cardColors(containerColor = VermelhoSeta.copy(alpha = 0.1f)),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text(
                                mensagemErro,
                                color = VermelhoSeta,
                                modifier = Modifier.padding(12.dp),
                                fontSize = 13.sp
                            )
                        }
                    }

                    Spacer(Modifier.height(4.dp))

                    // Botão Criar Conta
                    Button(
                        onClick = { viewModel.criarConta(aoContaCriada) },
                        modifier = Modifier.fillMaxWidth().height(54.dp),
                        enabled = viewModel.formularioValido() && !carregando,
                        colors = ButtonDefaults.buttonColors(containerColor = VerdeFundo),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        if (carregando) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(24.dp),
                                color = Color.White,
                                strokeWidth = 2.dp
                            )
                        } else {
                            Text("Criar minha conta", fontWeight = FontWeight.Bold,
                                fontSize = 16.sp, color = Color.White)
                        }
                    }

                    // Link para login
                    TextButton(
                        onClick = aoVoltar,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Já tenho conta → Entrar", color = VerdeFundo)
                    }
                }
            }
        }
    }
}