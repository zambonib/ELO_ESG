
package br.com.projeto.elo.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Memory
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import br.com.projeto.elo.ui.components.BarraNavegacaoElo
import br.com.projeto.elo.ui.theme.*
import br.com.projeto.elo.R
import br.com.projeto.elo.ui.screens.EconomizeViewModel

@Composable
fun EconomizeScreen(
    aoNavegar: (String) -> Unit = {},
    viewModel: EconomizeViewModel = hiltViewModel() // Conexão com o ViewModel real
) {
    // Coleta os dados em tempo real da API Gemini através do ViewModel
    val queryCalculadora by viewModel.queryCalculadora.collectAsState()
    val respostaCalculadora by viewModel.respostaCalculadora.collectAsState()
    val carregandoCalculadora by viewModel.carregandoCalculadora.collectAsState()

    Scaffold(
        bottomBar = {
            BarraNavegacaoElo(rotaAtual = "economize", aoNavegar = aoNavegar)
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(BackgroundLight)
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Brush.linearGradient(listOf(Color(0xFF059669), Color(0xFF10B981))))
                    .statusBarsPadding()
                    .padding(24.dp)
            ) {
                Column(modifier = Modifier.padding(top = 16.dp)) {
                    Text("Economize 🌱", color = Color.White, fontSize = 24.sp, fontWeight = FontWeight.Black)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text("Pequenas ações, grandes transformações.", color = Color.White.copy(alpha = 0.8f), fontSize = 14.sp)
                }
            }

            Column(
                modifier = Modifier.padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                SectionTitle(title = "Guia de economia", color = VerdeFundo)

                ExpandableGuideCard(
                    title = "Água: Consumo Consciente",
                    icon = Icons.Default.WaterDrop,
                    iconTint = Color(0xFF3B82F6),
                    bgColor = Color(0xFFEFF6FF),
                    borderColor = Color(0xFFBFDBFE),
                    shortDescription = "Reduza a conta de água com dicas simples para sua casa."
                )

                ExpandableGuideCard(
                    title = "Eletricidade: Conta Mais Leve",
                    icon = Icons.Default.Bolt,
                    iconTint = Color(0xFFEAB308),
                    bgColor = Color(0xFFFEFCE8),
                    borderColor = Color(0xFFFEF08A),
                    shortDescription = "Poupe energia e mantenha seu dinheiro no bolso."
                )

                ExpandableGuideCard(
                    title = "Tecnologia: Green IT",
                    icon = Icons.Default.Memory,
                    iconTint = Color(0xFF10B981),
                    bgColor = Color(0xFFECFDF5),
                    borderColor = Color(0xFFA7F3D0),
                    shortDescription = "Aprenda a usar a IA de forma eficiente e sustentável."
                )

                Spacer(modifier = Modifier.height(8.dp))

                SectionTitle(title = "Calculadora de economia de KWH", color = VerdeFundo)

                CalculadoraEconomiaAiCard(
                    query = queryCalculadora,
                    onQueryChanged = { viewModel.atualizarQueryCalculadora(it) },
                    response = respostaCalculadora,
                    isLoading = carregandoCalculadora,
                    onCalculateClick = { viewModel.calcularEconomiaAi(queryCalculadora) }
                )
            }
        }
    }
}

@Composable
private fun SectionTitle(title: String, color: Color) {
    Text(
        text = title,
        fontSize = 18.sp,
        fontWeight = FontWeight.Black,
        color = color,
        modifier = Modifier.padding(bottom = 4.dp, top = 8.dp)
    )
}

@Composable
private fun CalculadoraEconomiaAiCard(
    query: String,
    onQueryChanged: (String) -> Unit,
    response: String?,
    isLoading: Boolean,
    onCalculateClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, Color.LightGray.copy(alpha = 0.5f), RoundedCornerShape(16.dp)),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.AutoAwesome,
                    contentDescription = null,
                    tint = LaranjaBotao,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Pergunte ao Elo quanto você pode economizar",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1F2937)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = query,
                onValueChange = onQueryChanged,
                modifier = Modifier.fillMaxWidth(),
                placeholder = {
                    Text(
                        text = "Ex: \"Quanto economizo trocando 10 lâmpadas por LED?\"",
                        fontSize = 13.sp,
                        color = Color.Gray
                    )
                },
                shape = RoundedCornerShape(12.dp),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                keyboardActions = KeyboardActions(
                    onSearch = { if (!isLoading) onCalculateClick() }
                ),
                trailingIcon = {
                    if (query.isNotEmpty()) {
                        IconButton(onClick = { onQueryChanged("") }) {
                            Icon(Icons.Default.Close, contentDescription = "Limpar", tint = Color.Gray)
                        }
                    }
                },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = LaranjaBotao,
                    unfocusedBorderColor = Color.LightGray
                ),
                singleLine = false,
                maxLines = 3
            )

            Spacer(modifier = Modifier.height(12.dp))

            Button(
                onClick = onCalculateClick,
                modifier = Modifier.fillMaxWidth(),
                enabled = query.isNotBlank() && !isLoading,
                colors = ButtonDefaults.buttonColors(
                    containerColor = LaranjaBotao,
                    contentColor = Color.White,
                    disabledContainerColor = Color.LightGray
                ),
                shape = RoundedCornerShape(12.dp),
                contentPadding = PaddingValues(vertical = 12.dp)
            ) {
                if (isLoading) {
                    CircularProgressIndicator(modifier = Modifier.size(20.dp), color = Color.White, strokeWidth = 2.dp)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("O Elo está calculando...", fontSize = 14.sp, fontWeight = FontWeight.Bold)
                } else {
                    Icon(Icons.Default.AutoAwesome, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Calcular Economia com IA", fontSize = 14.sp, fontWeight = FontWeight.Bold)
                }
            }

            if (response != null || isLoading) {
                Spacer(modifier = Modifier.height(16.dp))
                HorizontalDivider(color = Color.LightGray.copy(alpha = 0.3f))
                Spacer(modifier = Modifier.height(16.dp))

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color(0xFFF9FAFB))
                        .border(1.dp, Color.LightGray.copy(alpha = 0.5f), RoundedCornerShape(12.dp))
                        .padding(16.dp)
                ) {
                    if (isLoading) {
                        Text(
                            text = "Aguarde, analisando sua pergunta...",
                            fontSize = 13.sp,
                            color = Color.Gray,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth()
                        )
                    } else if (response != null) {
                        Row {
                            Icon(
                                imageVector = Icons.Default.AutoAwesome,
                                contentDescription = null,
                                tint = LaranjaBotao,
                                modifier = Modifier.size(16.dp).padding(top = 2.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = response,
                                fontSize = 13.sp,
                                color = Color(0xFF4B5563),
                                lineHeight = 20.sp
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun ExpandableGuideCard(
    title: String,
    icon: ImageVector,
    iconTint: Color,
    bgColor: Color,
    borderColor: Color,
    shortDescription: String
) {
    var showDialog by remember { mutableStateOf(false) }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            properties = androidx.compose.ui.window.DialogProperties(usePlatformDefaultWidth = false),
            icon = { Icon(imageVector = icon, contentDescription = null, tint = iconTint, modifier = Modifier.size(28.dp)) },
            title = { Text(text = title, fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color(0xFF1F2937), textAlign = TextAlign.Center) },
            modifier = Modifier.fillMaxWidth(0.92f).wrapContentHeight().padding(16.dp),
            shape = RoundedCornerShape(24.dp),
            containerColor = Color.White,
            text = {
                Column(
                    modifier = Modifier.fillMaxWidth().padding(top = 8.dp).verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(20.dp)
                ) {
                    HorizontalDivider(color = borderColor)

                    val introText = when (title) {
                        "Água: Consumo Consciente" -> "Guias práticos de consumo consciente geram um duplo impacto real: reduzem a pegada ecológica da sua família e criam economia imediata no orçamento doméstico."
                        "Eletricidade: Conta Mais Leve" -> "Diferente de falsas ilusões financeiras geradas por simuladores de investimentos de curto prazo, economizar energia é um ganho garantido, ético e sem riscos."
                        "Tecnologia: Green IT" -> "Você sabia que o uso de IA consome energia real (e muita água!) em servidores pelo mundo? Veja como você pode economizar recursos:"
                        else -> ""
                    }

                    if (introText.isNotEmpty()) {
                        Text(text = introText, fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color(0xFF4B5563), lineHeight = 24.sp)
                        Spacer(modifier = Modifier.height(12.dp))
                        HorizontalDivider(color = borderColor)
                    }

                    val guideItems = when (title) {
                        "Água: Consumo Consciente" -> listOf(
                            Pair(R.drawable.vazamento, "Cheque vazamentos regularmente. Um pequeno gotejamento na torneira ou na válvula do vaso sanitário pode desperdiçar dezenas de litros por dia, inflando sua conta no final do mês sem que você perceba."),
                            Pair(R.drawable.banho, "Reduza o tempo no banho (1 minuto a menos faz diferença!). Além de economizar milhares de litros de água por mês em uma residência, você também reduz drasticamente o consumo de energia se utilizar chuveiro elétrico."),
                            Pair(R.drawable.economia, "O dinheiro economizado sobra para a subsistência da sua família. Pequenas mudanças de hábito geram uma economia cumulativa que, ao final de um ano, representa um alívio financeiro significativo para investir no que realmente importa.")
                        )
                        "Eletricidade: Conta Mais Leve" -> listOf(
                            Pair(R.drawable.led, "Troque lâmpadas antigas por LED. Elas consomem até 80% menos energia, possuem uma vida útil muito maior e não aquecem o ambiente, o que ajuda a manter a casa mais fresca."),
                            Pair(R.drawable.standby, "Desligue aparelhos em stand-by. Equipamentos conectados na tomada, mesmo desligados (como micro-ondas, TVs e carregadores), continuam consumindo energia de forma silenciosa. Retire-os da tomada quando não estiverem em uso."),
                            Pair(R.drawable.recursos, "Uma conta de luz mais barata significa mais recursos para sua rotina diária. Cada quilowatt-hora poupado é dinheiro que permanece no seu bolso, garantindo um orçamento mais folgado e seguro contra reajustes tarifários.")
                        )
                        "Tecnologia: Green IT" -> listOf(
                            Pair(R.drawable.objetividade, "Seja Objetivo: Ao enviar mensagens (prompts) para a IA, seja direto e evite informações repetitivas. Isso poupa 'tokens' e reduz o esforço de processamento. Lembre-se de que cada interação aciona servidores físicos que consomem eletricidade e água para resfriamento."),
                            Pair(R.drawable.eula, "Leitura de Contratos (EULA): Precisa que a IA analise Termos de Uso ou contratos longos? Envie apenas o trecho da sua dúvida. Enviar o documento gigante a cada pergunta gasta energia desnecessária e sobrecarrega a rede de dados global."),
                            Pair(R.drawable.cache_ia, "Aproveite o Cache (Contexto): Sempre que possível, continue no mesmo chat em vez de criar um novo e reenviar os mesmos arquivos. O sistema reaproveita o contexto salvo, economizando drasticamente os recursos dos Data Centers! Fazer escolhas inteligentes no digital também é sustentabilidade.")
                        )
                        else -> emptyList()
                    }

                    guideItems.forEach { (imageRes, itemText) -> GuideItem(imageRes = imageRes, text = itemText) }
                }
            },
            confirmButton = { TextButton(onClick = { showDialog = false }) { Text("Fechar", color = iconTint, fontWeight = FontWeight.Bold, fontSize = 16.sp) } }
        )
    }

    Column(
        modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(16.dp)).background(bgColor).border(1.dp, borderColor, RoundedCornerShape(16.dp)).clickable { showDialog = true }.padding(16.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier.size(40.dp).clip(RoundedCornerShape(10.dp)).background(Color.White).border(1.dp, borderColor, RoundedCornerShape(10.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(imageVector = icon, contentDescription = null, tint = iconTint, modifier = Modifier.size(24.dp))
            }
            Spacer(modifier = Modifier.width(12.dp))
            Text(text = title, fontSize = 15.sp, fontWeight = FontWeight.Bold, color = Color(0xFF1F2937))
        }
        Spacer(modifier = Modifier.height(12.dp))
        Text(text = shortDescription, fontSize = 13.sp, color = Color(0xFF1F2937), fontWeight = FontWeight.Medium, lineHeight = 18.sp)
    }
}

@Composable
private fun GuideItem(@androidx.annotation.DrawableRes imageRes: Int, text: String) {
    Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = "• $text", modifier = Modifier.fillMaxWidth(), fontSize = 14.sp, color = Color(0xFF4B5563), lineHeight = 22.sp, textAlign = TextAlign.Start)
        Spacer(modifier = Modifier.height(12.dp))
        Image(painter = androidx.compose.ui.res.painterResource(id = imageRes), contentDescription = null, contentScale = ContentScale.Fit, modifier = Modifier.fillMaxWidth(0.8f).height(110.dp).clip(RoundedCornerShape(12.dp)).padding(8.dp))
    }
}
