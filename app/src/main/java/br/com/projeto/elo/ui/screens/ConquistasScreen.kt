package br.com.projeto.elo.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.com.projeto.elo.ui.components.BarraNavegacaoElo
import br.com.projeto.elo.ui.theme.*

data class Conquista(
    val id: Int,
    val titulo: String,
    val descricao: String,
    val emoji: String,
    val xp: Int,
    val desbloqueada: Boolean,
    val progressoTexto: String
)

@Composable
fun ConquistasScreen(
    aoNavegar: (String) -> Unit = {}
) {
    val conquistas = listOf(
        Conquista(1, "Primeiro Passo", "Concluiu sua primeira aula de Educação Financeira", "🎓", 50, true, "Concluído"),
        Conquista(2, "Mestre do Registro", "Registrou mais de 5 lançamentos no Dashboard", "💰", 80, true, "Concluído"),
        Conquista(3, "Mente Blindada", "Acertou o quiz sobre Juros Compostos de primeira", "🚀", 100, true, "Concluído"),
        Conquista(4, "Eco Consciente", "Explorou as dicas de consumo sustentável no Economize", "🌱", 70, false, "3/3 dicas lidas"),
        Conquista(5, "Cidadão Informado", "Conheceu seus direitos e benefícios sociais no Social", "🤝", 60, true, "Concluído"),
        Conquista(6, "Reserva de Ouro", "Definiu e manteve uma meta de gastos no Finanças", "🏦", 120, false, "Em progresso")
    )

    Scaffold(
        bottomBar = {
            BarraNavegacaoElo(rotaAtual = "conquistas", aoNavegar = aoNavegar)
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(BackgroundLight)
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
        ) {
            // Header com Gradiente Dourado / Âmbar
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Brush.linearGradient(listOf(Color(0xFFD97706), Color(0xFFF59E0B))))
                    .statusBarsPadding()
                    .padding(24.dp)
            ) {
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text("Conquistas 🏆", color = Color.White, fontSize = 26.sp, fontWeight = FontWeight.ExtraBold)
                            Text("Sua evolução e recompensas no ELO", color = Color.White.copy(alpha = 0.9f), fontSize = 14.sp)
                        }
                        Box(
                            modifier = Modifier
                                .size(48.dp)
                                .clip(CircleShape)
                                .background(Color.White.copy(alpha = 0.2f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.EmojiEvents, contentDescription = null, tint = Color.White, modifier = Modifier.size(28.dp))
                        }
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    // Card de Nível e XP
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.95f))
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(Icons.Default.Star, contentDescription = null, tint = Color(0xFFF59E0B), modifier = Modifier.size(22.dp))
                                    Spacer(Modifier.width(6.dp))
                                    Text("Nível 3 — Guardião Financeiro", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = TextDark)
                                }
                                Text("360 / 500 XP", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = Color(0xFFD97706))
                            }
                            Spacer(Modifier.height(8.dp))
                            LinearProgressIndicator(
                                progress = { 0.72f },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(8.dp)
                                    .clip(RoundedCornerShape(4.dp)),
                                color = Color(0xFFF59E0B),
                                trackColor = Color(0xFFE5E7EB)
                            )
                        }
                    }
                }
            }

            // Lista de Medalhas e Desafios
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                Text("Medalhas Desbloqueadas", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = TextDark)

                conquistas.forEach { c ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = if (c.desbloqueada) Color.White else Color(0xFFF3F4F6)
                        ),
                        border = androidx.compose.foundation.BorderStroke(
                            1.dp,
                            if (c.desbloqueada) Color(0xFFFDE68A) else Color(0xFFE5E7EB)
                        )
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            // Badge Ícone
                            Box(
                                modifier = Modifier
                                    .size(48.dp)
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(if (c.desbloqueada) Color(0xFFFEF3C7) else Color(0xFFE5E7EB)),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    if (c.desbloqueada) c.emoji else "🔒",
                                    fontSize = 24.sp
                                )
                            }

                            Spacer(Modifier.width(14.dp))

                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    c.titulo,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 15.sp,
                                    color = if (c.desbloqueada) TextDark else TextMuted
                                )
                                Spacer(Modifier.height(2.dp))
                                Text(
                                    c.descricao,
                                    fontSize = 12.sp,
                                    color = TextMuted,
                                    lineHeight = 16.sp
                                )
                            }

                            Spacer(Modifier.width(8.dp))

                            // Tag de XP ou Status
                            if (c.desbloqueada) {
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(Color(0xFFD1FAE5))
                                        .padding(horizontal = 8.dp, vertical = 4.dp)
                                ) {
                                    Text(
                                        "+${c.xp} XP",
                                        color = VerdeFundo,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            } else {
                                Icon(Icons.Default.Lock, contentDescription = "Bloqueado", tint = TextMuted, modifier = Modifier.size(18.dp))
                            }
                        }
                    }
                }

                Spacer(Modifier.height(24.dp))
            }
        }
    }
}
