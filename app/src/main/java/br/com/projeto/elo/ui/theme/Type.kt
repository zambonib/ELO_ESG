package br.com.projeto.elo.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

// ── Tipografia Oficial do Material Theme 3 (ELO Design System) ─────────────────
val Typography = Typography(
    // Títulos Principais / Grandes Métricas (Ex: "Dashboard", Saldo Destaque)
    displayLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.ExtraBold,
        fontSize = 28.sp,
        lineHeight = 34.sp,
        letterSpacing = 0.sp
    ),
    displayMedium = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Bold,
        fontSize = 24.sp,
        lineHeight = 30.sp,
        letterSpacing = 0.sp
    ),
    displaySmall = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Bold,
        fontSize = 22.sp,
        lineHeight = 28.sp,
        letterSpacing = 0.sp
    ),

    // Cabeçalhos de Seções e Modais (Ex: "Trilha de Educação", "Lançamentos")
    headlineLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Black,
        fontSize = 20.sp,
        lineHeight = 26.sp,
        letterSpacing = 0.sp
    ),
    headlineMedium = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Bold,
        fontSize = 18.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.15.sp
    ),
    headlineSmall = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.SemiBold,
        fontSize = 16.sp,
        lineHeight = 22.sp,
        letterSpacing = 0.15.sp
    ),

    // Títulos de Cards e Itens de Lista (Ex: "Saldo do mês", "Mercado")
    titleLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Bold,
        fontSize = 16.sp,
        lineHeight = 22.sp,
        letterSpacing = 0.15.sp
    ),
    titleMedium = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Bold,
        fontSize = 15.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.1.sp
    ),
    titleSmall = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.SemiBold,
        fontSize = 14.sp,
        lineHeight = 18.sp,
        letterSpacing = 0.1.sp
    ),

    // Corpo de Texto e Explicações (Ex: Descrições de microaulas, subtítulos)
    bodyLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp
    ),
    bodyMedium = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.25.sp
    ),
    bodySmall = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.4.sp
    ),

    // Botões, Badges, Tags e Informações Auxiliares (Ex: "+50 XP", "1/5 concluídas")
    labelLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Bold,
        fontSize = 16.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.1.sp
    ),
    labelMedium = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Bold,
        fontSize = 13.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.5.sp
    ),
    labelSmall = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.SemiBold,
        fontSize = 10.sp,
        lineHeight = 13.sp,
        letterSpacing = 0.5.sp
    )
)

// ── Estilos Semânticos Diretos de Apoio (Atalhos do ELO) ────────────────────────
val TextoTituloTela     = TextStyle(fontFamily = FontFamily.Default, fontWeight = FontWeight.ExtraBold, fontSize = 28.sp)
val TextoSubtituloTela  = TextStyle(fontFamily = FontFamily.Default, fontWeight = FontWeight.Bold, fontSize = 20.sp)
val TextoSecao          = TextStyle(fontFamily = FontFamily.Default, fontWeight = FontWeight.Bold, fontSize = 18.sp)
val TextoCardTitulo     = TextStyle(fontFamily = FontFamily.Default, fontWeight = FontWeight.Bold, fontSize = 15.sp)
val TextoCardSubtitulo  = TextStyle(fontFamily = FontFamily.Default, fontWeight = FontWeight.Normal, fontSize = 12.sp)
val TextoValorDestaque  = TextStyle(fontFamily = FontFamily.Default, fontWeight = FontWeight.Bold, fontSize = 22.sp)
val TextoValorItem      = TextStyle(fontFamily = FontFamily.Default, fontWeight = FontWeight.Bold, fontSize = 15.sp)
val TextoBotaoPrincipal = TextStyle(fontFamily = FontFamily.Default, fontWeight = FontWeight.Bold, fontSize = 16.sp)
val TextoBadge          = TextStyle(fontFamily = FontFamily.Default, fontWeight = FontWeight.Bold, fontSize = 12.sp)
val TextoMicro          = TextStyle(fontFamily = FontFamily.Default, fontWeight = FontWeight.Medium, fontSize = 10.sp)
