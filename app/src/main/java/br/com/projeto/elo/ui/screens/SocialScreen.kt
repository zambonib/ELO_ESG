package br.com.projeto.elo.ui.screens

import android.content.Intent
import android.net.Uri
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.OpenInBrowser
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.com.projeto.elo.ui.components.BarraNavegacaoElo
import br.com.projeto.elo.ui.theme.*

/**
 * Modelo de dados para Benefícios Sociais.
 */
data class BenefitItem(
    val id: Int,
    val name: String,
    val icon: String,
    val color: Color,
    val bg: Color,
    val desc: String,
    val requirements: List<String>,
    val value: String,
    val where: String
)

/**
 * Tela de Apoio e Assistência Social (SocialScreen).
 */
@Composable
fun SocialScreen(
    aoNavegar: (String) -> Unit = {}
) {
    val context = LocalContext.current
    var expandedId by remember { mutableStateOf<Int?>(null) }
    var mostrarModalCras by remember { mutableStateOf(false) }

    val benefits = listOf(
        BenefitItem(
            id = 1,
            name = "Bolsa Família",
            icon = "👨‍👩‍👧‍👦",
            color = VerdeFundo,
            bg = Color(0xFFD1FAE5),
            desc = "Renda mensal para famílias em pobreza e extrema pobreza com crianças, gestantes ou nutrizes.",
            requirements = listOf(
                "Renda por pessoa de até R$ 218 (extrema pobreza) ou R$ 218–R$ 660 (pobreza)",
                "Cadastrar família no CadÚnico",
                "Manter crianças na escola (frequência mínima 60–75%)",
                "Seguir agenda de saúde (vacinas e pré-natal em dia)"
            ),
            value = "A partir de R$ 142/mês por família + benefícios adicionais por filho",
            where = "CRAS (Centro de Referência de Assistência Social) mais próximo ou gov.br"
        ),
        BenefitItem(
            id = 2,
            name = "BPC-LOAS",
            icon = "♿",
            color = Color(0xFF3B82F6),
            bg = Color(0xFFEFF6FF),
            desc = "Benefício de Prestação Continuada — 1 salário mínimo para idosos +65 anos ou pessoas com deficiência grave.",
            requirements = listOf(
                "Idoso com 65 anos ou mais, OU pessoa com deficiência grave (qualquer idade)",
                "Renda familiar por pessoa inferior a 1/4 do salário mínimo",
                "Não receber outro benefício previdenciário",
                "Estar inscrito no CadÚnico"
            ),
            value = "1 salário mínimo por mês (R$ 1.412 em 2024)",
            where = "Agência do INSS ou pelo Meu INSS (app e site)"
        ),
        BenefitItem(
            id = 3,
            name = "Auxílio Gás",
            icon = "🔥",
            color = Color(0xFFF59E0B),
            bg = Color(0xFFFEF3C7),
            desc = "Ajuda bimestral para compra de gás de cozinha (GLP) para famílias de baixa renda.",
            requirements = listOf(
                "Estar inscrito no CadÚnico",
                "Renda familiar por pessoa de até meio salário mínimo",
                "Famílias com integrante recebendo BPC também têm direito"
            ),
            value = "100% do preço médio do botijão de 13kg — pago a cada 2 meses",
            where = "Automático para quem está no CadÚnico. Consulte no CRAS ou gov.br"
        ),
        BenefitItem(
            id = 4,
            name = "Aluguel Social / MCMV",
            icon = "🏠",
            color = Color(0xFFEC4899),
            bg = Color(0xFFFDF2F8),
            desc = "Subsídio habitacional e auxílio aluguel para famílias sem casa própria em situação vulnerável.",
            requirements = listOf(
                "Não possuir imóvel próprio",
                "Renda familiar bruta até R$ 2.640/mês (Faixa 1)",
                "Estar inscrito no CadÚnico",
                "Sem ter sido beneficiado anteriormente por programa habitacional do governo"
            ),
            value = "Aluguel social: valor variável por município. MCMV: financiamento com subsídio de até 95%",
            where = "Prefeitura municipal / Secretaria de Habitação ou Caixa Econômica Federal"
        )
    )

    Scaffold(
        bottomBar = {
            BarraNavegacaoElo(rotaAtual = "social", aoNavegar = aoNavegar)
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(BackgroundLight)
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
        ) {
            // Header Azul Escuro
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Brush.linearGradient(listOf(Color(0xFF0F4C81), Color(0xFF1D6FA4))))
                    .statusBarsPadding()
                    .padding(24.dp)
            ) {
                Column {
                    Text("Assistência Social 🤝", color = Color.White, fontSize = 24.sp, fontWeight = FontWeight.Black)
                    Text("Conheça os benefícios que são seus por direito", color = Color.White.copy(alpha = 0.85f), fontSize = 14.sp)
                }
            }

            Column(
                modifier = Modifier.padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Card Informativo CadÚnico
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .background(Color(0xFFEFF6FF))
                        .border(1.5.dp, Color(0xFF93C5FD), RoundedCornerShape(16.dp))
                        .padding(16.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("💬", fontSize = 24.sp)
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(
                            "O primeiro passo para acessar a maioria dos benefícios é se cadastrar no CadÚnico. Vá até o CRAS da sua cidade.",
                            fontSize = 13.sp,
                            color = Color(0xFF1E40AF),
                            fontWeight = FontWeight.Medium,
                            lineHeight = 18.sp
                        )
                    }
                    Spacer(modifier = Modifier.height(14.dp))
                    Button(
                        onClick = { aoNavegar("cras_search") },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2563EB)),
                        modifier = Modifier.fillMaxWidth().height(48.dp),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Icon(Icons.Default.LocationOn, contentDescription = null, modifier = Modifier.size(18.dp))
                        Spacer(Modifier.width(8.dp))
                        Text("Buscar CRAS por CEP 📍", color = Color.White, fontWeight = FontWeight.Bold)
                    }
                }

                // Lista de Benefícios Expansíveis
                benefits.forEach { b ->
                    val isExpanded = expandedId == b.id

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(16.dp))
                            .background(Color.White)
                            .border(1.5.dp, if (isExpanded) b.color else Color(0xFFE5E7EB), RoundedCornerShape(16.dp))
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { expandedId = if (isExpanded) null else b.id }
                                .padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(48.dp)
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(b.bg),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(b.icon, fontSize = 24.sp)
                            }

                            Spacer(modifier = Modifier.width(12.dp))

                            Column(modifier = Modifier.weight(1f)) {
                                Text(b.name, fontSize = 15.sp, fontWeight = FontWeight.Black, color = Color(0xFF1F2937))
                                Text(b.desc, fontSize = 12.sp, color = Color(0xFF6B7280), maxLines = 1)
                            }

                            Icon(
                                imageVector = if (isExpanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                                contentDescription = "Expandir",
                                tint = b.color
                            )
                        }

                        AnimatedVisibility(visible = isExpanded) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 16.dp, vertical = 12.dp),
                                verticalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                HorizontalDivider(color = Color(0xFFE5E7EB))

                                Text("Quem tem direito?", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = b.color)
                                b.requirements.forEach { req ->
                                    Row(verticalAlignment = Alignment.Top) {
                                        Text("• ", color = b.color, fontWeight = FontWeight.Bold)
                                        Text(req, fontSize = 13.sp, color = Color(0xFF1F2937), lineHeight = 18.sp)
                                    }
                                }

                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clip(RoundedCornerShape(12.dp))
                                        .background(b.bg)
                                        .padding(12.dp)
                                ) {
                                    Column {
                                        Text("💰 Valor", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = b.color)
                                        Text(b.value, fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = Color(0xFF1F2937))
                                    }
                                }

                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clip(RoundedCornerShape(12.dp))
                                        .background(Color(0xFFF8FAFC))
                                        .border(1.dp, Color(0xFFE5E7EB), RoundedCornerShape(12.dp))
                                        .padding(12.dp)
                                ) {
                                    Column {
                                        Text("📍 Onde solicitar", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color(0xFF6B7280))
                                        Text(b.where, fontSize = 13.sp, color = Color(0xFF1F2937))
                                    }
                                }
                            }
                        }
                    }
                }

                Spacer(Modifier.height(24.dp))
            }
        }
    }

    // Modal de Informações e Localização do CRAS
    if (mostrarModalCras) {
        AlertDialog(
            onDismissRequest = { mostrarModalCras = false },
            title = {
                Text("Como encontrar o CRAS 📍", fontWeight = FontWeight.Bold, fontSize = 18.sp)
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text(
                        "O CRAS (Centro de Referência de Assistência Social) é o local onde você realiza o CadÚnico e solicita benefícios.",
                        fontSize = 13.sp,
                        color = TextDark,
                        lineHeight = 18.sp
                    )

                    Text(
                        "📄 Documentos necessários para levar:\n" +
                                "• RG e CPF de todos da família\n" +
                                "• Comprovante de Residência\n" +
                                "• Certidão de Nascimento das crianças\n" +
                                "• Carteira de Trabalho (se houver)",
                        fontSize = 12.sp,
                        color = Color(0xFF4B5563),
                        lineHeight = 18.sp
                    )

                    Spacer(Modifier.height(4.dp))

                    Button(
                        onClick = {
                            try {
                                val gmmIntentUri = Uri.parse("geo:0,0?q=CRAS")
                                val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
                                context.startActivity(mapIntent)
                            } catch (e: Exception) {
                                val webIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.google.com/maps/search/CRAS"))
                                context.startActivity(webIntent)
                            }
                            mostrarModalCras = false
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2563EB)),
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Icon(Icons.Default.LocationOn, null, modifier = Modifier.size(18.dp))
                        Spacer(Modifier.width(6.dp))
                        Text("Ver CRAS no Google Maps 🗺️", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                    }

                    OutlinedButton(
                        onClick = {
                            try {
                                val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://cadunico.dataprev.gov.br/"))
                                context.startActivity(browserIntent)
                            } catch (e: Exception) {
                                e.printStackTrace()
                            }
                            mostrarModalCras = false
                        },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Icon(Icons.Default.OpenInBrowser, null, modifier = Modifier.size(18.dp))
                        Spacer(Modifier.width(6.dp))
                        Text("Acessar Portal do CadÚnico 🌐", fontSize = 13.sp)
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { mostrarModalCras = false }) {
                    Text("Fechar")
                }
            }
        )
    }
}
