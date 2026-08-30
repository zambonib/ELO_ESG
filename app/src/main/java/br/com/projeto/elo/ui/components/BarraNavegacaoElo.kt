package br.com.projeto.elo.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.Eco
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.People
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.com.projeto.elo.ui.theme.VerdeFundo

/**
 * Barra de navegação inferior compartilhada entre as 6 telas do ELO.
 * Abas: Início, Educação, Finanças, Conquistas, Economize e Social.
 */
@Composable
fun BarraNavegacaoElo(
    rotaAtual: String,
    aoNavegar: (String) -> Unit
) {
    NavigationBar(containerColor = Color.White, tonalElevation = 8.dp) {
        val coresSelecionado = NavigationBarItemDefaults.colors(
            selectedIconColor = VerdeFundo,
            selectedTextColor = VerdeFundo,
            indicatorColor = Color.Transparent
        )

        // 1. Início (Dashboard)
        NavigationBarItem(
            selected = rotaAtual == "dashboard",
            onClick = { if (rotaAtual != "dashboard") aoNavegar("dashboard") },
            icon = { Icon(Icons.Default.Home, contentDescription = "Início") },
            label = { Text("Início", maxLines = 1, fontSize = 9.sp) },
            colors = coresSelecionado
        )

        // 2. Educação (Aulas e Quizzes)
        NavigationBarItem(
            selected = rotaAtual == "educacao",
            onClick = { if (rotaAtual != "educacao") aoNavegar("educacao") },
            icon = { Icon(Icons.Default.MenuBook, contentDescription = "Educação") },
            label = { Text("Educação", maxLines = 1, fontSize = 9.sp) },
            colors = coresSelecionado
        )

        // 3. Finanças (Orçamento e Metas)
        NavigationBarItem(
            selected = rotaAtual == "financas",
            onClick = { if (rotaAtual != "financas") aoNavegar("financas") },
            icon = { Icon(Icons.Default.AccountBalanceWallet, contentDescription = "Finanças") },
            label = { Text("Finanças", maxLines = 1, fontSize = 9.sp) },
            colors = coresSelecionado
        )

        // 4. Conquistas (Badges e XP)
        NavigationBarItem(
            selected = rotaAtual == "conquistas",
            onClick = { if (rotaAtual != "conquistas") aoNavegar("conquistas") },
            icon = { Icon(Icons.Default.EmojiEvents, contentDescription = "Conquistas") },
            label = { Text("Conquistas", maxLines = 1, fontSize = 8.5.sp) },
            colors = coresSelecionado
        )

        // 5. Economize (Sustentabilidade ESG)
        NavigationBarItem(
            selected = rotaAtual == "economize",
            onClick = { if (rotaAtual != "economize") aoNavegar("economize") },
            icon = { Icon(Icons.Default.Eco, contentDescription = "Economize") },
            label = { Text("Economize", maxLines = 1, fontSize = 8.5.sp) },
            colors = coresSelecionado
        )

        // 6. Social (Benefícios Sociais / CRAS)
        NavigationBarItem(
            selected = rotaAtual == "social",
            onClick = { if (rotaAtual != "social") aoNavegar("social") },
            icon = { Icon(Icons.Default.People, contentDescription = "Social") },
            label = { Text("Social", maxLines = 1, fontSize = 9.sp) },
            colors = coresSelecionado
        )
    }
}
