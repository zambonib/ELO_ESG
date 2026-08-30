package br.com.projeto.elo.ui.auth

import br.com.projeto.elo.ui.theme.LaranjaBotao
import br.com.projeto.elo.ui.theme.VerdeCard
import br.com.projeto.elo.ui.theme.VerdeFundo
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.com.projeto.elo.R
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.hilt.navigation.compose.hiltViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginTela(
    viewModel: LoginViewModel = hiltViewModel(),
    aoNavegarParaDashboard: () -> Unit,
    aoNavegarParaCadastro: () -> Unit
) {
    // Controla se a aba inferior (BottomSheet) está aberta
    var mostrarAbaLogin by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(VerdeFundo)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(48.dp))

        Image(
            painter = painterResource(id = R.drawable.elo_logo),
            contentDescription = "Logo do ELO",
            contentScale = ContentScale.Fit,
            modifier = Modifier.size(120.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Image(
            painter = painterResource(id = R.drawable.elo_letras),
            contentDescription = "ELO",
            contentScale = ContentScale.Fit,
            modifier = Modifier.height(48.dp)
        )
        Text("Conectando você ao seu futuro", fontSize = 16.sp, color = Color.White, modifier = Modifier.padding(bottom = 32.dp))

        BeneficioCard(emoji = "📚", texto = "Aprenda finanças em minutos por dia")
        BeneficioCard(emoji = "💰", texto = "Controle seus gastos com facilidade")
        BeneficioCard(emoji = "🏆", texto = "Ganhe conquistas e evolua")
        BeneficioCard(emoji = "🤝", texto = "Conheça seus direitos e benefícios")

        Spacer(modifier = Modifier.weight(1f))

        Button(
            onClick = { aoNavegarParaCadastro() },
            modifier = Modifier.fillMaxWidth().height(56.dp),
            colors = ButtonDefaults.buttonColors(containerColor = LaranjaBotao),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text("Criar minha conta grátis", color = Color.Black, fontSize = 16.sp, fontWeight = FontWeight.Bold)
        }

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedButton(
            onClick = { mostrarAbaLogin = true }, // Abre a aba de login!
            modifier = Modifier.fillMaxWidth().height(56.dp).border(1.dp, Color.White, RoundedCornerShape(12.dp)),
            colors = ButtonDefaults.outlinedButtonColors(containerColor = Color.Transparent),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text("Já tenho conta — Entrar", color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Bold)
        }

        Spacer(modifier = Modifier.height(32.dp))
    }

    // A Aba inferior onde o usuário digita E-mail e Senha
    if (mostrarAbaLogin) {
        ModalBottomSheet(onDismissRequest = { mostrarAbaLogin = false }) {
            AbaDeLogin(viewModel, aoNavegarParaDashboard)
        }
    }
}

@Composable
fun AbaDeLogin(viewModel: LoginViewModel, aoNavegarParaDashboard: () -> Unit) {
    val email by viewModel.email.collectAsState()
    val senha by viewModel.senha.collectAsState()
    val mensagemErro by viewModel.mensagemErro.collectAsState()

    Column(
        modifier = Modifier.fillMaxWidth().padding(24.dp).padding(bottom = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Bem-vindo de volta!", fontSize = 24.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = email,
            onValueChange = { viewModel.atualizarEmail(it) },
            label = { Text("E-mail") },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email,
                imeAction = ImeAction.Next  // ← avança para o próximo campo
            ),
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = senha,
            onValueChange = { viewModel.atualizarSenha(it) },
            label = { Text("Senha") },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Done  // ← botão "Concluir"
            ),
            keyboardActions = KeyboardActions(
                onDone = { viewModel.realizarLogin(aoSucesso = aoNavegarParaDashboard) }
            ),
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth()
        )

        // Caso o Firebase Gere  erro.
        if (mensagemErro.isNotEmpty()) {
            Text(mensagemErro, color = MaterialTheme.colorScheme.error, modifier = Modifier.padding(top = 8.dp))
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = { viewModel.realizarLogin(aoSucesso = aoNavegarParaDashboard) },
            modifier = Modifier.fillMaxWidth().height(50.dp),
            colors = ButtonDefaults.buttonColors(containerColor = LaranjaBotao)
        ) {
            Text("Entrar", color = Color.Black, fontWeight = FontWeight.Bold, fontSize = 16.sp)
        }
    }
}

@Composable
fun BeneficioCard(emoji: String, texto: String) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 6.dp).background(VerdeCard, shape = RoundedCornerShape(12.dp)).padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = emoji, fontSize = 20.sp)
        Spacer(modifier = Modifier.width(16.dp))
        Text(texto, color = Color.White, fontSize = 15.sp, fontWeight = FontWeight.Medium)
    }
}
