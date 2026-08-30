package br.com.projeto.elo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import br.com.projeto.elo.ui.auth.CadastroTela
import br.com.projeto.elo.ui.auth.LoginTela
<<<<<<< HEAD
import br.com.projeto.elo.ui.dashboard.DashboardTela
import br.com.projeto.elo.ui.financas.FinancasTela
=======
import br.com.projeto.elo.ui.cras.CrasSearchScreen
import br.com.projeto.elo.ui.dashboard.DashboardTela
>>>>>>> 3df5725dd0882ba487761f3c383b1179530a7e89
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
<<<<<<< HEAD
                            aoNavegar = { rota ->
                                navController.navigate(rota) { launchSingleTop = true }
=======
                            aoNavegarParaCras = {
                                navController.navigate("cras_search")
>>>>>>> 3df5725dd0882ba487761f3c383b1179530a7e89
                            }
                        )
                    }

<<<<<<< HEAD
                    composable("financas") {
                        FinancasTela(
                            aoNavegar = { rota ->
                                navController.navigate(rota) { launchSingleTop = true }
                            }
=======
                    composable("cras_search") {
                        CrasSearchScreen(
                            aoVoltar = { navController.popBackStack() }
>>>>>>> 3df5725dd0882ba487761f3c383b1179530a7e89
                        )
                    }
                }
            }
        }
    }
}