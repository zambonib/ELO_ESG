package br.com.projeto.elo.ui.dashboard

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import br.com.projeto.elo.dominio.modelo.TipoTransacao
import br.com.projeto.elo.dominio.modelo.Transacao
import br.com.projeto.elo.ui.theme.LaranjaBotao
import br.com.projeto.elo.ui.theme.VerdeCard
import br.com.projeto.elo.ui.theme.VerdeFundo
import br.com.projeto.elo.ui.theme.VermelhoSeta
import coil.compose.AsyncImage
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardTela(
    viewModel: DashboardViewModel = hiltViewModel(),
    aoSair: () -> Unit = {},
    aoNavegarParaCras: () -> Unit = {}
) {
    // Contexto do Android fica aqui dentro das chaves!
    val context = LocalContext.current

    val transacoes by viewModel.transacoes.collectAsState()
    val carregandoIa by viewModel.carregandoIa.collectAsState()
    val textoBusca by viewModel.textoBusca.collectAsState()
    val receitaDoMes by viewModel.receitaDoMes.collectAsState()
    val despesaDoMes by viewModel.despesaDoMes.collectAsState()
    val fotoUri by viewModel.fotoUri.collectAsState()
    val nomeUsuario by viewModel.nomeUsuario.collectAsState()

    var mostrarMenuAvatar by remember { mutableStateOf(false) }
    var mostrarModalIa by remember { mutableStateOf(false) }
    var textoIa by remember { mutableStateOf("") }
    var mostrarDialogNome by remember { mutableStateOf(false) }
    var mostrarDialogSenha by remember { mutableStateOf(false) }
    var transacaoParaEditar by remember { mutableStateOf<Transacao?>(null) }
    var abaSelecionada by remember { mutableIntStateOf(0) }

    val saldo = receitaDoMes - despesaDoMes
    val porcentagemGasta = if (receitaDoMes > 0)
        (despesaDoMes / receitaDoMes).toFloat().coerceIn(0f, 1f) else 0f
    val emojiUsuario = if (nomeUsuario == "Maria") "👱‍♀️" else "🧑"
    val fmt = NumberFormat.getCurrencyInstance(Locale("pt", "BR"))

    // Launcher da galeria de fotos com permissão persistente
    val galeriaLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            try {
                context.contentResolver.takePersistableUriPermission(
                    it,
                    android.content.Intent.FLAG_GRANT_READ_URI_PERMISSION
                )
            } catch (e: Exception) {
                e.printStackTrace()
            }
            viewModel.salvarFoto(it)
        }
    }

    Scaffold(
        floatingActionButton = {
            if (abaSelecionada == 0) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    FloatingActionButton(
                        onClick = { mostrarModalIa = true },
                        containerColor = LaranjaBotao,
                        contentColor = Color.White
                    ) {
                        Icon(Icons.Default.AutoAwesome, contentDescription = "Registrar Lançamento")
                    }
                    Spacer(Modifier.height(4.dp))
                    Text(
                        text = "Clique para\nregistrar",
                        color = Color.White,
                        fontSize = 10.sp,
                        textAlign = TextAlign.Center,
                        lineHeight = 13.sp
                    )
                }
            }
        },
        bottomBar = {
            NavigationBar(containerColor = Color.White, tonalElevation = 8.dp) {
                val activeColors = NavigationBarItemDefaults.colors(selectedIconColor = VerdeFundo, selectedTextColor = VerdeFundo, indicatorColor = Color.Transparent)
                NavigationBarItem(selected = abaSelecionada == 0, onClick = { abaSelecionada = 0 }, icon = { Icon(Icons.Default.Home, "") }, label = { Text("Início", maxLines = 1, fontSize = 10.sp) }, colors = activeColors)
                NavigationBarItem(selected = abaSelecionada == 1, onClick = { abaSelecionada = 1 }, icon = { Icon(Icons.Default.MenuBook, "") }, label = { Text("Educação", maxLines = 1, fontSize = 9.sp) }, colors = activeColors)
                NavigationBarItem(selected = abaSelecionada == 2, onClick = { abaSelecionada = 2 }, icon = { Icon(Icons.Default.AccountBalanceWallet, "") }, label = { Text("Finanças", maxLines = 1, fontSize = 10.sp) }, colors = activeColors)
                NavigationBarItem(selected = abaSelecionada == 3, onClick = { abaSelecionada = 3 }, icon = { Icon(Icons.Default.EmojiEvents, "") }, label = { Text("Conquistas", maxLines = 1, fontSize = 9.sp) }, colors = activeColors)
                NavigationBarItem(selected = abaSelecionada == 4, onClick = { abaSelecionada = 4 }, icon = { Icon(Icons.Default.Eco, "") }, label = { Text("Economize", maxLines = 1, fontSize = 9.sp) }, colors = activeColors)
                NavigationBarItem(selected = abaSelecionada == 5, onClick = { abaSelecionada = 5 }, icon = { Icon(Icons.Default.People, "") }, label = { Text("Social", maxLines = 1, fontSize = 10.sp) }, colors = activeColors)
            }
        }
    ) { paddingValues ->
        Box(modifier = Modifier.fillMaxSize().padding(paddingValues)) {
            when (abaSelecionada) {
                0 -> {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(VerdeFundo)
                            .verticalScroll(rememberScrollState())
                    ) {
                        // --- CABEÇALHO ---
                        Row(
                            modifier = Modifier.fillMaxWidth().padding(24.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text("Olá, $nomeUsuario! 👋", color = Color.White, fontSize = 16.sp)
                    Text("Dashboard", color = Color.White, fontSize = 28.sp, fontWeight = FontWeight.ExtraBold)
                }

                // AVATAR COM MENU
                Box {
                    Box(
                        modifier = Modifier
                            .size(52.dp)
                            .clip(CircleShape)
                            .background(VerdeCard)
                            .clickable { mostrarMenuAvatar = true },
                        contentAlignment = Alignment.Center
                    ) {
                        if (fotoUri != null) {
                            AsyncImage(
                                model = fotoUri,
                                contentDescription = "Foto de perfil",
                                contentScale = ContentScale.Crop,
                                modifier = Modifier.fillMaxSize()
                            )
                        } else {
                            Text(emojiUsuario, fontSize = 24.sp)
                        }
                    }

                    DropdownMenu(
                        expanded = mostrarMenuAvatar,
                        onDismissRequest = { mostrarMenuAvatar = false }
                    ) {
                        DropdownMenuItem(
                            text = { Text("📷  Alterar Foto") },
                            onClick = { mostrarMenuAvatar = false; galeriaLauncher.launch("image/*") }
                        )
                        DropdownMenuItem(
                            text = { Text("✏️  Trocar Nome") },
                            onClick = { mostrarMenuAvatar = false; mostrarDialogNome = true }
                        )
                        DropdownMenuItem(
                            text = { Text("🔒  Trocar Senha") },
                            onClick = { mostrarMenuAvatar = false; mostrarDialogSenha = true }
                        )
                        HorizontalDivider()
                        DropdownMenuItem(
                            text = { Text("🚪  Sair do Aplicativo", color = VermelhoSeta) },
                            onClick = { mostrarMenuAvatar = false; viewModel.sairDoAplicativo(aoSair) },
                            leadingIcon = { Icon(Icons.Default.ExitToApp, null, tint = VermelhoSeta) }
                        )
                    }
                }
            }

            // --- CARDS DE SALDO ---
            Row(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                CardResumo("Saldo do mês", fmt.format(saldo), "Lançamentos do mês", Modifier.weight(1f))
                CardResumo("Total despesas", fmt.format(despesaDoMes), "Saídas do mês", Modifier.weight(1f))
            }

            Spacer(Modifier.height(20.dp))

            // --- BARRA DE RECEITA TOTAL DO MÊS ---
            Column(modifier = Modifier.padding(horizontal = 24.dp)) {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("Receita total do mês", color = Color.White, fontSize = 14.sp)
                    Text(fmt.format(receitaDoMes), color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                }
                Spacer(Modifier.height(8.dp))
                LinearProgressIndicator(
                    progress = { porcentagemGasta },
                    modifier = Modifier.fillMaxWidth().height(8.dp).clip(RoundedCornerShape(4.dp)),
                    color = LaranjaBotao,
                    trackColor = VerdeCard
                )
                Spacer(Modifier.height(6.dp))
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("Gasto: ${(porcentagemGasta * 100).toInt()}%", color = Color.White.copy(alpha = 0.7f), fontSize = 12.sp)
                    Text("Saldo: ${(100 - (porcentagemGasta * 100)).toInt()}%", color = Color.White.copy(alpha = 0.7f), fontSize = 12.sp)
                }
            }

            Spacer(Modifier.height(28.dp))

            // --- ÁREA BRANCA ---
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White, RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp))
                    .padding(24.dp)
            ) {
                Column {
                    // CAMPO DE BUSCA
                    OutlinedTextField(
                        value = textoBusca,
                        onValueChange = { viewModel.atualizarBusca(it) },
                        modifier = Modifier.fillMaxWidth(),
                        placeholder = { Text("Buscar por nome, valor ou categoria...") },
                        leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                        trailingIcon = {
                            if (textoBusca.isNotEmpty()) {
                                IconButton(onClick = { viewModel.atualizarBusca("") }) {
                                    Icon(Icons.Default.Close, null)
                                }
                            }
                        },
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = VerdeFundo,
                            unfocusedBorderColor = Color.LightGray
                        ),
                        singleLine = true
                    )

                    Spacer(Modifier.height(20.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Lançamentos", fontSize = 20.sp, fontWeight = FontWeight.Bold)
                        Text("${transacoes.size} registros", color = Color.Gray, fontSize = 14.sp)
                    }

                    Spacer(Modifier.height(12.dp))

                    if (transacoes.isEmpty()) {
                        Text(
                            if (textoBusca.isBlank())
                                "Nenhum lançamento ainda. Use o botão ✨ para registrar!"
                            else
                                "Nenhum resultado para \"$textoBusca\".",
                            color = Color.Gray, fontSize = 14.sp
                        )
                    } else {
                        transacoes.forEach { transacao ->
                            ItemLancamento(
                                transacao = transacao,
                                formatadorMoeda = fmt,
                                onEditar = { transacaoParaEditar = it },
                                onExcluir = { viewModel.excluirTransacao(it) }
                            )
                            HorizontalDivider(color = Color.LightGray.copy(alpha = 0.5f))
                        }
                    }
                }
            }
                    }
                }
                1 -> br.com.projeto.elo.ui.screens.EducationScreen()
                2 -> Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) { Text("Finanças: Em breve...") }
                3 -> Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) { Text("Conquistas: Em breve...") }
                4 -> br.com.projeto.elo.ui.screens.EconomizeScreen()
                5 -> br.com.projeto.elo.ui.screens.SocialScreen(onNavigate = { screen ->
                    if (screen == br.com.projeto.elo.navigation.Screen.CRAS_SEARCH) {
                        aoNavegarParaCras()
                    }
                })
            }
        }
    }

    // --- MODAL DA IA ---
    if (mostrarModalIa) {
        AlertDialog(
            onDismissRequest = { mostrarModalIa = false },
            title = { Text("Me diga qual o nome e o valor do lançamento", fontWeight = FontWeight.Bold) },
            text = {
                Column {
                    Text(
                        "Digite como se estivesse conversando com alguém.\nEx: \"Gastei 10 reais na padaria\" ou \"Recebi 100 reais da empresa ELO\" e a IA vai fazer o lançamento.",
                        fontSize = 13.sp,
                        color = Color.Gray
                    )
                    Spacer(Modifier.height(12.dp))
                    OutlinedTextField(
                        value = textoIa,
                        onValueChange = { textoIa = it },
                        modifier = Modifier.fillMaxWidth(),
                        placeholder = { Text("Digite aqui...") },
                        shape = RoundedCornerShape(12.dp),
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Send),
                        keyboardActions = KeyboardActions(
                            onSend = {
                                if (textoIa.isNotBlank() && !carregandoIa) {
                                    viewModel.registrarGastoComInteligencia(textoIa)
                                    textoIa = ""
                                    mostrarModalIa = false
                                }
                            }
                        )
                    )
                    if (carregandoIa) {
                        Spacer(Modifier.height(16.dp))
                        LinearProgressIndicator(modifier = Modifier.fillMaxWidth(), color = VerdeFundo)
                        Spacer(Modifier.height(4.dp))
                        Text("A IA está analisando...", fontSize = 12.sp, color = Color.Gray)
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = { viewModel.registrarGastoComInteligencia(textoIa); textoIa = ""; mostrarModalIa = false },
                    enabled = textoIa.isNotBlank() && !carregandoIa,
                    colors = ButtonDefaults.buttonColors(containerColor = VerdeFundo)
                ) { Text("Enviar para IA") }
            },
            dismissButton = {
                TextButton(onClick = { mostrarModalIa = false }) { Text("Cancelar") }
            }
        )
    }

    // --- DIALOG DE EDIÇÃO ---
    transacaoParaEditar?.let { t ->
        var descricao by remember(t.id) { mutableStateOf(t.descricao) }
        var valor by remember(t.id) { mutableStateOf(t.valor.toString()) }
        var categoria by remember(t.id) { mutableStateOf(t.categoria) }
        var dataMs by remember(t.id) { mutableStateOf(t.data.takeIf { it > 0 } ?: System.currentTimeMillis()) }
        var mostrarDatePicker by remember { mutableStateOf(false) }

        val dataFormatada = remember(dataMs) {
            SimpleDateFormat("dd/MM/yyyy", Locale("pt", "BR")).format(Date(dataMs))
        }

        // DatePickerDialog
        if (mostrarDatePicker) {
            val datePickerState = rememberDatePickerState(initialSelectedDateMillis = dataMs)
            DatePickerDialog(
                onDismissRequest = { mostrarDatePicker = false },
                confirmButton = {
                    TextButton(onClick = {
                        dataMs = datePickerState.selectedDateMillis ?: dataMs
                        mostrarDatePicker = false
                    }) { Text("OK") }
                },
                dismissButton = {
                    TextButton(onClick = { mostrarDatePicker = false }) { Text("Cancelar") }
                }
            ) {
                DatePicker(state = datePickerState)
            }
        }

        AlertDialog(
            onDismissRequest = { transacaoParaEditar = null },
            title = { Text("Editar Lançamento", fontWeight = FontWeight.Bold) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = descricao,
                        onValueChange = { descricao = it },
                        label = { Text("Descrição") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp)
                    )
                    OutlinedTextField(
                        value = valor,
                        onValueChange = { valor = it },
                        label = { Text("Valor (R$)") },
                        modifier = Modifier.fillMaxWidth(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                        shape = RoundedCornerShape(12.dp)
                    )
                    OutlinedTextField(
                        value = categoria,
                        onValueChange = { categoria = it },
                        label = { Text("Categoria") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp)
                    )
                    // Campo de Data clicável
                    OutlinedTextField(
                        value = dataFormatada,
                        onValueChange = {},
                        label = { Text("Data") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { mostrarDatePicker = true },
                        enabled = false,
                        trailingIcon = {
                            Icon(
                                Icons.Default.CalendarMonth,
                                contentDescription = "Selecionar data",
                                tint = VerdeFundo
                            )
                        },
                        colors = OutlinedTextFieldDefaults.colors(
                            disabledTextColor = Color.Black,
                            disabledBorderColor = Color.Gray,
                            disabledLabelColor = Color.Gray
                        ),
                        shape = RoundedCornerShape(12.dp)
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.editarTransacao(
                            t.copy(
                                descricao = descricao,
                                valor = valor.toDoubleOrNull() ?: t.valor,
                                categoria = categoria,
                                data = dataMs
                            )
                        )
                        transacaoParaEditar = null
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = VerdeFundo)
                ) { Text("Salvar") }
            },
            dismissButton = {
                TextButton(onClick = { transacaoParaEditar = null }) { Text("Cancelar") }
            }
        )
    }

    // --- DIALOG TROCAR NOME ---
    if (mostrarDialogNome) {
        var novoNome by remember { mutableStateOf("") }
        var carregando by remember { mutableStateOf(false) }
        AlertDialog(
            onDismissRequest = { mostrarDialogNome = false },
            title = { Text("Trocar Nome") },
            text = {
                OutlinedTextField(
                    value = novoNome,
                    onValueChange = { novoNome = it },
                    label = { Text("Novo nome") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                    keyboardActions = KeyboardActions(
                        onDone = {
                            if (novoNome.isNotBlank() && !carregando) {
                                carregando = true
                                viewModel.trocarNome(novoNome) {
                                    carregando = false
                                    mostrarDialogNome = false
                                }
                            }
                        }
                    )
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        carregando = true
                        viewModel.trocarNome(novoNome) { sucesso ->
                            carregando = false
                            mostrarDialogNome = false
                        }
                    },
                    enabled = novoNome.isNotBlank() && !carregando,
                    colors = ButtonDefaults.buttonColors(containerColor = VerdeFundo)
                ) { Text(if (carregando) "Salvando..." else "Salvar") }
            },
            dismissButton = { TextButton(onClick = { mostrarDialogNome = false }) { Text("Cancelar") } }
        )
    }

    // --- DIALOG TROCAR SENHA ---
    if (mostrarDialogSenha) {
        var senhaAtual by remember { mutableStateOf("") }
        var novaSenha by remember { mutableStateOf("") }
        var mensagem by remember { mutableStateOf("") }
        var carregando by remember { mutableStateOf(false) }
        AlertDialog(
            onDismissRequest = { mostrarDialogSenha = false },
            title = { Text("Trocar Senha") },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(value = senhaAtual, onValueChange = { senhaAtual = it },
                        label = { Text("Senha atual") }, modifier = Modifier.fillMaxWidth(),
                        visualTransformation = PasswordVisualTransformation(), shape = RoundedCornerShape(12.dp))
                    OutlinedTextField(value = novaSenha, onValueChange = { novaSenha = it },
                        label = { Text("Nova senha") }, modifier = Modifier.fillMaxWidth(),
                        visualTransformation = PasswordVisualTransformation(), shape = RoundedCornerShape(12.dp))
                    if (mensagem.isNotEmpty())
                        Text(mensagem, color = if (mensagem.contains("sucesso")) VerdeFundo else VermelhoSeta, fontSize = 13.sp)
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        carregando = true
                        viewModel.trocarSenha(senhaAtual, novaSenha) { sucesso, msg ->
                            carregando = false
                            mensagem = msg
                            if (sucesso) mostrarDialogSenha = false
                        }
                    },
                    enabled = senhaAtual.isNotBlank() && novaSenha.length >= 6 && !carregando,
                    colors = ButtonDefaults.buttonColors(containerColor = VerdeFundo)
                ) { Text(if (carregando) "Alterando..." else "Alterar") }
            },
            dismissButton = { TextButton(onClick = { mostrarDialogSenha = false }) { Text("Cancelar") } }
        )
    }
}

// --- COMPONENTES ---

@Composable
fun CardResumo(titulo: String, valor: String, subtitulo: String, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = VerdeCard),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(titulo, color = Color.White.copy(alpha = 0.8f), fontSize = 12.sp)
            Spacer(Modifier.height(4.dp))
            // Texto quebra linha se o valor for negativo/longo
            Text(valor, color = Color.White, fontSize = 22.sp, fontWeight = FontWeight.Bold, maxLines = 2)
            Spacer(Modifier.height(8.dp))
            Text(subtitulo, color = Color.White.copy(alpha = 0.9f), fontSize = 11.sp)
        }
    }
}

@Composable
fun ItemLancamento(
    transacao: Transacao,
    formatadorMoeda: NumberFormat,
    onEditar: (Transacao) -> Unit,
    onExcluir: (Transacao) -> Unit
) {
    val isReceita = transacao.tipo == TipoTransacao.RECEITA
    val corSeta = if (isReceita) VerdeFundo else VermelhoSeta
    val valorTexto = if (isReceita)
        formatadorMoeda.format(transacao.valor)
    else
        "-${formatadorMoeda.format(transacao.valor)}"

    val dataFormatada = remember(transacao.data) {
        SimpleDateFormat("dd/MM/yy", Locale("pt", "BR")).format(Date(transacao.data))
    }

    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Ícone seta colorida
        Box(
            modifier = Modifier.size(40.dp).background(corSeta.copy(alpha = 0.12f), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = if (isReceita) Icons.Default.ArrowUpward else Icons.Default.ArrowDownward,
                contentDescription = null,
                tint = corSeta,
                modifier = Modifier.size(22.dp)
            )
        }

        Spacer(Modifier.width(12.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(transacao.descricao, fontWeight = FontWeight.Bold, fontSize = 15.sp)
            Text("${transacao.categoria} · $dataFormatada", fontSize = 12.sp, color = Color.Gray)
        }

        // Valor com cor
        Text(
            valorTexto,
            fontWeight = FontWeight.Bold,
            fontSize = 15.sp,
            color = if (isReceita) VerdeFundo else VermelhoSeta
        )

        Spacer(Modifier.width(4.dp))

        // Botão Editar
        IconButton(onClick = { onEditar(transacao) }, modifier = Modifier.size(32.dp)) {
            Icon(Icons.Default.Edit, contentDescription = "Editar", tint = Color.Gray, modifier = Modifier.size(18.dp))
        }

        // Botão Excluir
        IconButton(onClick = { onExcluir(transacao) }, modifier = Modifier.size(32.dp)) {
            Icon(Icons.Default.Delete, contentDescription = "Excluir", tint = VermelhoSeta, modifier = Modifier.size(18.dp))
        }
    }
}
