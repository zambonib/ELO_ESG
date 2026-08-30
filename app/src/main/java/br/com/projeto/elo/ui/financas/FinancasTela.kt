package br.com.projeto.elo.ui.financas

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Flag
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import br.com.projeto.elo.dominio.modelo.CategoriaTotal
import br.com.projeto.elo.ui.components.BarraNavegacaoElo
import br.com.projeto.elo.ui.dashboard.CardResumo
import br.com.projeto.elo.ui.theme.LaranjaBotao
import br.com.projeto.elo.ui.theme.VerdeCard
import br.com.projeto.elo.ui.theme.VerdeFundo
import br.com.projeto.elo.ui.theme.VermelhoSeta
import java.text.NumberFormat
import java.util.Date
import java.util.Locale

// Paleta usada para diferenciar as fatias do gráfico de categorias
private val CoresCategorias = listOf(
    Color(0xFF238650), Color(0xFFF39200), Color(0xFF3F51B5), Color(0xFF9C27B0),
    Color(0xFF00ACC1), Color(0xFFE53935), Color(0xFF8D6E63), Color(0xFF43A047)
)

@Composable
fun FinancasTela(
    viewModel: FinancasViewModel = hiltViewModel(),
    aoNavegar: (String) -> Unit = {}
) {
    val mesAnchor by viewModel.mesAnchor.collectAsState()
    val despesas by viewModel.despesas.collectAsState()
    val saldo by viewModel.saldo.collectAsState()
    val taxaPoupanca by viewModel.taxaPoupanca.collectAsState()
    val gastosPorCategoria by viewModel.gastosPorCategoria.collectAsState()
    val orcamentos by viewModel.orcamentos.collectAsState()
    val scoreSaude by viewModel.scoreSaude.collectAsState()
    val evolucao by viewModel.evolucao6Meses.collectAsState()
    val dicaIa by viewModel.dicaIa.collectAsState()
    val carregandoDica by viewModel.carregandoDica.collectAsState()

    val fmt = remember { NumberFormat.getCurrencyInstance(Locale("pt", "BR")) }

    var categoriaMeta by remember { mutableStateOf<String?>(null) }

    Scaffold(
        bottomBar = { BarraNavegacaoElo(rotaAtual = "financas", aoNavegar = aoNavegar) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(VerdeFundo)
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
        ) {
            // --- CABEÇALHO + SELETOR DE MÊS ---
            Column(modifier = Modifier.fillMaxWidth().padding(24.dp)) {
                Text("Finanças", color = Color.White, fontSize = 28.sp, fontWeight = FontWeight.ExtraBold)
                Text("Entenda e planeje o seu dinheiro", color = Color.White.copy(alpha = 0.85f), fontSize = 14.sp)
                Spacer(Modifier.height(16.dp))
                SeletorMes(mesAnchor, onVoltar = viewModel::voltarMes, onAvancar = viewModel::avancarMes)
            }

            // --- CARDS DE RESUMO ---
            Row(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                CardResumo("Saldo do mês", fmt.format(saldo), "Receitas - despesas", Modifier.weight(1f))
                CardResumo(
                    "Taxa de poupança",
                    "${(taxaPoupanca * 100).toInt()}%",
                    "Do que você recebeu",
                    Modifier.weight(1f)
                )
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
                    CardSaudeFinanceira(
                        score = scoreSaude,
                        dica = dicaIa,
                        carregando = carregandoDica,
                        onGerarDica = viewModel::gerarDicaFinanceira
                    )

                    Spacer(Modifier.height(28.dp))

                    // --- GASTOS POR CATEGORIA ---
                    TituloSecao("Gastos por categoria")
                    Spacer(Modifier.height(12.dp))
                    if (gastosPorCategoria.isEmpty()) {
                        TextoVazio("Nenhuma despesa neste mês. Registre lançamentos no Dashboard.")
                    } else {
                        GraficoRosca(gastosPorCategoria, total = despesas)
                        Spacer(Modifier.height(16.dp))
                        gastosPorCategoria.forEachIndexed { index, item ->
                            LinhaCategoria(
                                item = item,
                                total = despesas,
                                cor = CoresCategorias[index % CoresCategorias.size],
                                formatadorMoeda = fmt,
                                onDefinirMeta = { categoriaMeta = item.categoria }
                            )
                        }
                    }

                    Spacer(Modifier.height(28.dp))

                    // --- ORÇAMENTOS POR CATEGORIA ---
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        TituloSecao("Metas de gasto")
                        TextButton(onClick = { categoriaMeta = "" }) { Text("+ Nova meta") }
                    }
                    Spacer(Modifier.height(4.dp))
                    if (orcamentos.isEmpty()) {
                        TextoVazio("Defina um limite de gasto por categoria para não estourar o orçamento.")
                    } else {
                        orcamentos.forEach { orc ->
                            LinhaOrcamento(
                                orcamento = orc,
                                formatadorMoeda = fmt,
                                onExcluir = { viewModel.removerLimite(orc.categoria) },
                                onEditar = { categoriaMeta = orc.categoria }
                            )
                        }
                    }

                    Spacer(Modifier.height(28.dp))

                    // --- EVOLUÇÃO 6 MESES ---
                    TituloSecao("Evolução dos últimos 6 meses")
                    Spacer(Modifier.height(4.dp))
                    LegendaEvolucao()
                    Spacer(Modifier.height(12.dp))
                    GraficoEvolucao(evolucao)
                }
            }
        }
    }

    // --- DIÁLOGO DEFINIR/EDITAR META ---
    categoriaMeta?.let { categoriaInicial ->
        DialogoMeta(
            categoriaInicial = categoriaInicial,
            onConfirmar = { categoria, valor ->
                viewModel.definirLimite(categoria, valor)
                categoriaMeta = null
            },
            onCancelar = { categoriaMeta = null }
        )
    }
}

// ─────────────────────────────────────────────────────────────────────
// COMPONENTES
// ─────────────────────────────────────────────────────────────────────

@Composable
private fun SeletorMes(mesAnchor: Long, onVoltar: () -> Unit, onAvancar: () -> Unit) {
    val label = remember(mesAnchor) {
        java.text.SimpleDateFormat("MMMM 'de' yyyy", Locale("pt", "BR"))
            .format(Date(mesAnchor))
            .replaceFirstChar { it.uppercase() }
    }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(VerdeCard)
            .padding(horizontal = 8.dp, vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = onVoltar) {
            Icon(Icons.Default.ChevronLeft, contentDescription = "Mês anterior", tint = Color.White)
        }
        Text(label, color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Bold)
        IconButton(onClick = onAvancar) {
            Icon(Icons.Default.ChevronRight, contentDescription = "Próximo mês", tint = Color.White)
        }
    }
}

@Composable
private fun CardSaudeFinanceira(
    score: Int,
    dica: String?,
    carregando: Boolean,
    onGerarDica: () -> Unit
) {
    val (rotulo, cor) = when {
        score >= 70 -> "Saudável" to VerdeFundo
        score >= 40 -> "Atenção" to LaranjaBotao
        else -> "Crítico" to VermelhoSeta
    }
    Card(
        colors = CardDefaults.cardColors(containerColor = cor.copy(alpha = 0.08f)),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text("Saúde financeira", fontSize = 14.sp, color = Color.Gray)
                    Text(rotulo, fontSize = 20.sp, fontWeight = FontWeight.ExtraBold, color = cor)
                }
                Text("$score/100", fontSize = 26.sp, fontWeight = FontWeight.ExtraBold, color = cor)
            }
            Spacer(Modifier.height(8.dp))
            LinearProgressIndicator(
                progress = { score / 100f },
                modifier = Modifier.fillMaxWidth().height(8.dp).clip(RoundedCornerShape(4.dp)),
                color = cor,
                trackColor = cor.copy(alpha = 0.2f)
            )
            Spacer(Modifier.height(12.dp))

            if (dica != null) {
                Text("💡 $dica", fontSize = 13.sp, color = Color.DarkGray)
                Spacer(Modifier.height(8.dp))
            }

            Button(
                onClick = onGerarDica,
                enabled = !carregando,
                colors = ButtonDefaults.buttonColors(containerColor = LaranjaBotao),
                shape = RoundedCornerShape(12.dp)
            ) {
                Icon(Icons.Default.AutoAwesome, contentDescription = null, tint = Color.White)
                Spacer(Modifier.width(8.dp))
                Text(
                    if (carregando) "Gerando dica..." else if (dica == null) "Gerar dica com IA" else "Nova dica",
                    color = Color.White
                )
            }
        }
    }
}

@Composable
private fun GraficoRosca(itens: List<CategoriaTotal>, total: Double) {
    Box(
        modifier = Modifier.fillMaxWidth().height(180.dp),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.size(160.dp)) {
            val larguraTraco = 34.dp.toPx()
            val diametro = size.minDimension - larguraTraco
            val canto = Offset((size.width - diametro) / 2f, (size.height - diametro) / 2f)
            val tamanho = Size(diametro, diametro)
            var inicio = -90f
            if (total <= 0.0) {
                drawArc(
                    color = Color.LightGray.copy(alpha = 0.4f),
                    startAngle = 0f, sweepAngle = 360f, useCenter = false,
                    topLeft = canto, size = tamanho, style = Stroke(larguraTraco, cap = StrokeCap.Round)
                )
            } else {
                itens.forEachIndexed { index, item ->
                    val varredura = (item.total / total).toFloat() * 360f
                    drawArc(
                        color = CoresCategorias[index % CoresCategorias.size],
                        startAngle = inicio, sweepAngle = varredura, useCenter = false,
                        topLeft = canto, size = tamanho, style = Stroke(larguraTraco, cap = StrokeCap.Butt)
                    )
                    inicio += varredura
                }
            }
        }
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text("Total", fontSize = 12.sp, color = Color.Gray)
            Text(
                NumberFormat.getCurrencyInstance(Locale("pt", "BR")).format(total),
                fontSize = 16.sp, fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
private fun LinhaCategoria(
    item: CategoriaTotal,
    total: Double,
    cor: Color,
    formatadorMoeda: NumberFormat,
    onDefinirMeta: () -> Unit
) {
    val percentual = if (total > 0) (item.total / total * 100).toInt() else 0
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(modifier = Modifier.size(12.dp).clip(CircleShape).background(cor))
        Spacer(Modifier.width(12.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(item.categoria, fontWeight = FontWeight.SemiBold, fontSize = 15.sp)
            Text("$percentual% do total", fontSize = 12.sp, color = Color.Gray)
        }
        Text(formatadorMoeda.format(item.total), fontWeight = FontWeight.Bold, fontSize = 15.sp)
        IconButton(onClick = onDefinirMeta, modifier = Modifier.size(32.dp)) {
            Icon(Icons.Default.Flag, contentDescription = "Definir meta", tint = Color.Gray, modifier = Modifier.size(18.dp))
        }
    }
}

@Composable
private fun LinhaOrcamento(
    orcamento: OrcamentoComGasto,
    formatadorMoeda: NumberFormat,
    onExcluir: () -> Unit,
    onEditar: () -> Unit
) {
    val cor = if (orcamento.estourou) VermelhoSeta else VerdeFundo
    Column(modifier = Modifier.fillMaxWidth().padding(vertical = 10.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(orcamento.categoria, fontWeight = FontWeight.SemiBold, fontSize = 15.sp)
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    "${formatadorMoeda.format(orcamento.gasto)} / ${formatadorMoeda.format(orcamento.limite)}",
                    fontSize = 13.sp, color = cor, fontWeight = FontWeight.SemiBold
                )
                IconButton(onClick = onEditar, modifier = Modifier.size(30.dp)) {
                    Icon(Icons.Default.Flag, contentDescription = "Editar meta", tint = Color.Gray, modifier = Modifier.size(16.dp))
                }
                IconButton(onClick = onExcluir, modifier = Modifier.size(30.dp)) {
                    Icon(Icons.Default.Delete, contentDescription = "Remover meta", tint = VermelhoSeta, modifier = Modifier.size(16.dp))
                }
            }
        }
        Spacer(Modifier.height(6.dp))
        LinearProgressIndicator(
            progress = { orcamento.progresso.coerceIn(0f, 1f) },
            modifier = Modifier.fillMaxWidth().height(8.dp).clip(RoundedCornerShape(4.dp)),
            color = cor,
            trackColor = Color.LightGray.copy(alpha = 0.4f)
        )
        if (orcamento.estourou) {
            Spacer(Modifier.height(4.dp))
            Text("⚠ Você ultrapassou o limite desta categoria", fontSize = 11.sp, color = VermelhoSeta)
        }
    }
}

@Composable
private fun GraficoEvolucao(meses: List<MesResumo>) {
    if (meses.isEmpty()) {
        TextoVazio("Sem dados suficientes para o gráfico.")
        return
    }
    val maxValor = remember(meses) {
        (meses.maxOfOrNull { maxOf(it.receitas, it.despesas) } ?: 0.0).coerceAtLeast(1.0)
    }
    Canvas(modifier = Modifier.fillMaxWidth().height(140.dp)) {
        val larguraColuna = size.width / meses.size
        val larguraBarra = larguraColuna * 0.28f
        val espaco = larguraColuna * 0.08f
        meses.forEachIndexed { index, mes ->
            val centro = larguraColuna * index + larguraColuna / 2f
            val alturaReceita = (mes.receitas / maxValor).toFloat() * size.height
            val alturaDespesa = (mes.despesas / maxValor).toFloat() * size.height
            // Receita (verde) à esquerda do centro
            drawRect(
                color = VerdeFundo,
                topLeft = Offset(centro - larguraBarra - espaco / 2f, size.height - alturaReceita),
                size = Size(larguraBarra, alturaReceita)
            )
            // Despesa (vermelho) à direita do centro
            drawRect(
                color = VermelhoSeta,
                topLeft = Offset(centro + espaco / 2f, size.height - alturaDespesa),
                size = Size(larguraBarra, alturaDespesa)
            )
        }
    }
    Spacer(Modifier.height(6.dp))
    Row(modifier = Modifier.fillMaxWidth()) {
        meses.forEach { mes ->
            val rotulo = remember(mes.anchor) {
                java.text.SimpleDateFormat("MMM", Locale("pt", "BR"))
                    .format(Date(mes.anchor))
                    .replaceFirstChar { it.uppercase() }
            }
            Text(
                rotulo,
                modifier = Modifier.weight(1f),
                fontSize = 11.sp,
                color = Color.Gray,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
private fun LegendaEvolucao() {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(Modifier.size(10.dp).clip(CircleShape).background(VerdeFundo))
        Spacer(Modifier.width(4.dp))
        Text("Receitas", fontSize = 12.sp, color = Color.Gray)
        Spacer(Modifier.width(16.dp))
        Box(Modifier.size(10.dp).clip(CircleShape).background(VermelhoSeta))
        Spacer(Modifier.width(4.dp))
        Text("Despesas", fontSize = 12.sp, color = Color.Gray)
    }
}

@Composable
private fun DialogoMeta(
    categoriaInicial: String,
    onConfirmar: (String, Double) -> Unit,
    onCancelar: () -> Unit
) {
    var categoria by remember { mutableStateOf(categoriaInicial) }
    var valor by remember { mutableStateOf("") }
    AlertDialog(
        onDismissRequest = onCancelar,
        title = { Text("Definir meta de gasto", fontWeight = FontWeight.Bold) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    value = categoria,
                    onValueChange = { categoria = it },
                    label = { Text("Categoria") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp)
                )
                OutlinedTextField(
                    value = valor,
                    onValueChange = { valor = it },
                    label = { Text("Limite mensal (R$)") },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp)
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val v = valor.replace(",", ".").toDoubleOrNull() ?: 0.0
                    onConfirmar(categoria, v)
                },
                enabled = categoria.isNotBlank() && (valor.replace(",", ".").toDoubleOrNull() ?: 0.0) > 0,
                colors = ButtonDefaults.buttonColors(containerColor = VerdeFundo)
            ) { Text("Salvar") }
        },
        dismissButton = { TextButton(onClick = onCancelar) { Text("Cancelar") } }
    )
}

@Composable
private fun TituloSecao(texto: String) {
    Text(texto, fontSize = 20.sp, fontWeight = FontWeight.Bold)
}

@Composable
private fun TextoVazio(texto: String) {
    Text(texto, color = Color.Gray, fontSize = 14.sp)
}
