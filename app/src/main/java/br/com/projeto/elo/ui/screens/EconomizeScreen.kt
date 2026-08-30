package br.com.projeto.elo.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.Memory
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.com.projeto.elo.ui.theme.*

/**
 * Tela Economize.
 * Focada em guias de economia: Água, Eletricidade e Tecnologia.
 */
@Composable
fun EconomizeScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF9FAFB)) // BackgroundLight
            .verticalScroll(rememberScrollState())
            .padding(bottom = 90.dp)
    ) {
        // Header Verde (Economize)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Brush.linearGradient(listOf(Color(0xFF059669), Color(0xFF10B981))))
                .padding(20.dp)
        ) {
            Column(modifier = Modifier.padding(top = 16.dp)) {
                Text(
                    "Economize 🌱",
                    color = Color.White,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Black
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    "Pequenas ações, grandes transformações.",
                    color = Color.White.copy(alpha = 0.8f),
                    fontSize = 14.sp
                )
            }
        }

        Column(
            modifier = Modifier.padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            SectionTitle(title = "Guia de economia", color = VerdeFundo)

            // ÁGUA
            ExpandableGuideCard(
                title = "Água: Consumo Consciente",
                icon = Icons.Default.WaterDrop,
                iconTint = Color(0xFF3B82F6),
                bgColor = Color(0xFFEFF6FF),
                borderColor = Color(0xFFBFDBFE),
                shortDescription = "Reduza a conta de água com dicas simples para sua casa.",
                detailedContent = "Guias práticos de consumo consciente geram um duplo impacto real: reduzem a pegada ecológica da sua família e criam economia imediata no orçamento doméstico.\n\n" +
                        "• Cheque vazamentos regularmente.\n" +
                        "• Reduza o tempo no banho (1 minuto a menos faz diferença!).\n" +
                        "• O dinheiro economizado sobra para a subsistência da sua família."
            )

            // ELETRICIDADE
            ExpandableGuideCard(
                title = "Eletricidade: Conta Mais Leve",
                icon = Icons.Default.Bolt,
                iconTint = Color(0xFFEAB308),
                bgColor = Color(0xFFFEFCE8),
                borderColor = Color(0xFFFEF08A),
                shortDescription = "Poupe energia e mantenha seu dinheiro no bolso.",
                detailedContent = "Diferente de falsas ilusões financeiras geradas por simuladores de investimentos de curto prazo, economizar energia é um ganho garantido, ético e sem riscos.\n\n" +
                        "• Troque lâmpadas antigas por LED.\n" +
                        "• Desligue aparelhos em stand-by.\n" +
                        "• Uma conta de luz mais barata significa mais recursos para sua rotina diária."
            )

            // TECNOLOGIA
            ExpandableGuideCard(
                title = "Tecnologia: Green IT",
                icon = Icons.Default.Memory,
                iconTint = Color(0xFF10B981),
                bgColor = Color(0xFFECFDF5),
                borderColor = Color(0xFFA7F3D0),
                shortDescription = "Aprenda a usar a Inteligência Artificial de forma eficiente e sustentável.",
                detailedContent = "Você sabia que o uso de IA consome energia real (e muita água!) em servidores pelo mundo? Veja como você pode economizar recursos:\n\n" +
                        "• Seja Objetivo: Ao enviar mensagens (prompts) para a IA, seja direto e evite informações repetitivas. Isso poupa 'tokens' e reduz o esforço de processamento.\n" +
                        "• Leitura de Contratos (EULA): Precisa que a IA analise Termos de Uso ou contratos longos? Envie apenas o trecho da sua dúvida. Enviar o documento gigante a cada pergunta gasta energia desnecessária.\n" +
                        "• Aproveite o Cache (Contexto): Sempre que possível, continue no mesmo chat em vez de criar um novo e reenviar os mesmos arquivos. O sistema reaproveita o contexto salvo, economizando drasticamente os recursos dos Data Centers!"
            )
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
private fun ExpandableGuideCard(
    title: String,
    icon: ImageVector,
    iconTint: Color,
    bgColor: Color,
    borderColor: Color,
    shortDescription: String,
    detailedContent: String
) {
    var expanded by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(bgColor)
            .border(1.dp, borderColor, RoundedCornerShape(16.dp))
            .clickable { expanded = !expanded }
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(Color.White)
                        .border(1.dp, borderColor, RoundedCornerShape(10.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = iconTint,
                        modifier = Modifier.size(24.dp)
                    )
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(
                        text = title,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF1F2937) // TextDark
                    )
                }
            }
            Icon(
                imageVector = if (expanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                contentDescription = if (expanded) "Recolher" else "Expandir",
                tint = iconTint
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = shortDescription,
            fontSize = 13.sp,
            color = Color(0xFF1F2937), // TextDark
            fontWeight = FontWeight.Medium,
            lineHeight = 18.sp
        )

        AnimatedVisibility(visible = expanded) {
            Column {
                Spacer(modifier = Modifier.height(12.dp))
                HorizontalDivider(color = borderColor)
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = detailedContent,
                    fontSize = 13.sp,
                    color = Color(0xFF6B7280), // TextMuted
                    lineHeight = 20.sp
                )
            }
        }
    }
}
