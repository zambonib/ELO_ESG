package br.com.projeto.elo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import br.com.projeto.elo.ui.auth.CadastroTela
import br.com.projeto.elo.ui.auth.LoginTela
import br.com.projeto.elo.ui.dashboard.DashboardTela
import br.com.projeto.elo.ui.financas.FinancasTela
import br.com.projeto.elo.ui.screens.EducationScreen
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

                    composable("educacao") {
                        EducationScreen(
                            aoNavegar = { rota ->
                                navController.navigate(rota) { launchSingleTop = true }
                            }
                        )
                    }

                    composable("financas") {
                        FinancasTela(
                            aoNavegar = { rota ->
                                navController.navigate(rota) { launchSingleTop = true }
                            }
                        )
                    }
                }
            }
        }
    }
}
