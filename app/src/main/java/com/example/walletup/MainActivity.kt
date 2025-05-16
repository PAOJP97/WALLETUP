package com.example.walletup

import android.content.Context
import android.os.Bundle
import android.os.SystemClock
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.example.walletup.ui.theme.WalletUpTheme
import com.example.walletup.utils.NotificationWorker
import com.example.walletup.utils.Timer
import com.example.walletup.utils.WidgetWorker
import com.example.walletup.views.AccountsView
import com.example.walletup.views.AddAccountView
import com.example.walletup.views.AddBudgetView
import com.example.walletup.views.BudgetsView
import com.example.walletup.views.BurgerMenuView
import com.example.walletup.views.DashboardView
import com.example.walletup.views.GraphsView
import com.example.walletup.views.SplashView
import com.example.walletup.views.TransactionView
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import com.google.firebase.FirebaseApp
import dagger.hilt.android.AndroidEntryPoint
import java.util.concurrent.TimeUnit

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    companion object {
        var allowNotifications: Boolean = false
        var allowWidget: Boolean = false
        var mensajeNotificacion: String = String()
    }

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        FirebaseApp.initializeApp(this)

        Timer.start(segundos = 120) {
            if (MainActivity.allowNotifications) {
                scheduleNotification(this, 5, TimeUnit.MINUTES)
            }
        }
        val request = OneTimeWorkRequestBuilder<WidgetWorker>().build()
        WorkManager.getInstance(this).enqueue(request)

        setContent {
            WalletUpTheme {
                val navController = rememberNavController()
                val tipoTransaccion = remember { mutableIntStateOf(0) }
                NavHost(navController, startDestination = "splash") {
                    composable("splash") {
                        SplashView(
                            onFinish = {
                                navController.navigate(route = "dashboard")
                            }
                        )
                    }
                    composable("dashboard") {
                        DashboardView(
                            onNewTransaction = {
                                tipoTransaccion.intValue = it
                                navController.navigate(route = "transaction")
                            },
                            onOpenMenu = {
                                navController.navigate(route = "menu")
                            }
                        )
                    }
                    composable("transaction") {
                        TransactionView(
                            tipoTransaccion = if (tipoTransaccion.intValue == 0) "G" else "I",
                            onAddTransaction = {
                                backToHome(navController)
                            },
                            onBack = {
                                backToHome(navController)
                            }
                        )
                    }
                    composable(route = "account") {
                        AccountsView(
                            onNewAccount = {
                                navController.navigate(route = "add-account")
                            },
                            onBack = {
                                backToHome(navController)
                            }
                        )
                    }
                    composable(route = "add-account") {
                        AddAccountView(
                            onAddAccount = {
                                backToHome(navController)
                            },
                            onBack = {
                                navController.popBackStack()
                            }
                        )
                    }
                    composable(route = "menu") {
                        BurgerMenuView(
                            onBack = {
                                backToHome(navController)
                            },
                            onCuentas = {
                                navController.navigate(route = "account")
                            },
                            onGraficos = {
                                navController.navigate(route = "graph")
                            },
                            onPresupuestos = {
                                navController.navigate(route = "budget")
                            }
                        )
                    }
                    composable(route = "graph") {
                        GraphsView(
                            onBack = {
                                navController.popBackStack()
                            }
                        )
                    }
                    composable(route = "budget") {
                        BudgetsView(
                            onNewBudget = {
                                navController.navigate(route = "add-budget")
                            },
                            onBack = {
                                backToHome(navController)
                            }
                        )
                    }
                    composable(route = "add-budget") {
                        AddBudgetView(
                            onAddBudget = {
                                backToHome(navController)
                            },
                            onBack = {
                                navController.popBackStack()
                            }
                        )
                    }
                }
            }
        }
    }
}

fun scheduleNotification(context: Context, delay: Long, unit: TimeUnit) {
    val workRequest = OneTimeWorkRequestBuilder<NotificationWorker>()
        .setInitialDelay(delay, unit)
        .addTag("notification_work")
        .build()

    WorkManager.getInstance(context).enqueue(workRequest)
}

fun removeScheduledNotification(context: Context) {
    WorkManager.getInstance(context).cancelAllWorkByTag("notification_work")
}

fun backToHome(navController: NavController) {
    navController.popBackStack(route = "dashboard", false)
}