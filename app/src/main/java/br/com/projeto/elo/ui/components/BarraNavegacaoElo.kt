package br.com.projeto.elo.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Insights
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import br.com.projeto.elo.ui.theme.VerdeFundo

/**
 * Barra de navegação inferior compartilhada entre as telas do ELO.
 * As abas de Início e Finanças navegam via [aoNavegar]; Trilha, Simular e Conquistas
 * ainda não foram implementadas e permanecem inertes.
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

        NavigationBarItem(
            selected = rotaAtual == "dashboard",
            onClick = { if (rotaAtual != "dashboard") aoNavegar("dashboard") },
            icon = { Icon(Icons.Default.Home, contentDescription = "Início") },
            label = { Text("Início") },
            colors = coresSelecionado
        )
        NavigationBarItem(
            selected = false,
            onClick = {},
            icon = { Icon(Icons.Default.MenuBook, contentDescription = "Trilha") },
            label = { Text("Trilha") }
        )
        NavigationBarItem(
            selected = rotaAtual == "financas",
            onClick = { if (rotaAtual != "financas") aoNavegar("financas") },
            icon = { Icon(Icons.Default.AccountBalanceWallet, contentDescription = "Finanças") },
            label = { Text("Finanças") },
            colors = coresSelecionado
        )
        NavigationBarItem(
            selected = false,
            onClick = {},
            icon = { Icon(Icons.Default.Insights, contentDescription = "Simular") },
            label = { Text("Simular") }
        )
        NavigationBarItem(
            selected = false,
            onClick = {},
            icon = { Icon(Icons.Default.EmojiEvents, contentDescription = "Conquistas") },
            label = { Text("Conquistas") }
        )
    }
}
