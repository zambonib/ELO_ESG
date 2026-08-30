package br.com.projeto.elo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import br.com.projeto.elo.ui.auth.CadastroTela
import br.com.projeto.elo.ui.auth.LoginTela
import br.com.projeto.elo.ui.cras.CrasSearchScreen
import br.com.projeto.elo.ui.dashboard.DashboardTela
import br.com.projeto.elo.ui.financas.FinancasTela
import br.com.projeto.elo.ui.screens.ConquistasScreen
import br.com.projeto.elo.ui.screens.EconomizeScreen
import br.com.projeto.elo.ui.screens.EducationScreen
import br.com.projeto.elo.ui.screens.SocialScreen
import br.com.projeto.elo.ui.theme.ELOTheme
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ELOTheme {
                val navController = rememberNavController()
                val usuarioAtual = FirebaseAuth.getInstance().currentUser
                val telaInicial = if (usuarioAtual != null) "dashboard" else "login"

                NavHost(navController = navController, startDestination = telaInicial) {

                    // 1. Login
                    composable("login") {
                        LoginTela(
                            aoNavegarParaDashboard = {
                                navController.navigate("dashboard") {
                                    popUpTo("login") { inclusive = true }
                                }
                            },
                            aoNavegarParaCadastro = {
                                navController.navigate("cadastro")
                            }
                        )
                    }

                    // 2. Cadastro
                    composable("cadastro") {
                        CadastroTela(
                            aoContaCriada = {
                                navController.navigate("dashboard") {
                                    popUpTo("login") { inclusive = true }
                                }
                            },
                            aoVoltar = { navController.popBackStack() }
                        )
                    }

                    // 3. Início (Dashboard)
                    composable("dashboard") {
                        DashboardTela(
                            aoSair = {
                                navController.navigate("login") {
                                    popUpTo("dashboard") { inclusive = true }
                                }
                            },
                            aoNavegar = { rota ->
                                navController.navigate(rota) { launchSingleTop = true }
                            }
                        )
                    }

                    // 4. Educação
                    composable("educacao") {
                        EducationScreen(
                            aoNavegar = { rota ->
                                navController.navigate(rota) { launchSingleTop = true }
                            }
                        )
                    }

                    // 5. Finanças
                    composable("financas") {
                        FinancasTela(
                            aoNavegar = { rota ->
                                navController.navigate(rota) { launchSingleTop = true }
                            }
                        )
                    }

                    // 6. Conquistas
                    composable("conquistas") {
                        ConquistasScreen(
                            aoNavegar = { rota ->
                                navController.navigate(rota) { launchSingleTop = true }
                            }
                        )
                    }

                    // 7. Economize
                    composable("economize") {
                        EconomizeScreen(
                            aoNavegar = { rota ->
                                navController.navigate(rota) { launchSingleTop = true }
                            }
                        )
                    }

                    // 8. Social
                    composable("social") {
                        SocialScreen(
                            aoNavegar = { rota ->
                                navController.navigate(rota) { launchSingleTop = true }
                            }
                        )
                    }

                    // 9. Busca do CRAS por CEP
                    composable("cras_search") {
                        CrasSearchScreen(
                            aoVoltar = { navController.popBackStack() }
                        )
                    }
                }
            }
        }
    }
}
