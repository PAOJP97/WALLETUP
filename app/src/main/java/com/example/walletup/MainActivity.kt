package com.example.walletup

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.walletup.ui.theme.WalletUpTheme
import com.example.walletup.views.DashboardView
import com.example.walletup.views.SplashView

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        var currentPage = Screen.SPLASH
        setContent {
            WalletUpTheme {

                /*
                SplashView(
                    onFinish = {
                        currentPage = Screen.DASHBOARD
                    }
                )
                */

                DashboardView()
                /*
                if (currentPage == Screen.SPLASH) {
                    SplashView(
                        onFinish = {
                            currentPage = Screen.DASHBOARD
                        }
                    )
                }
                if (currentPage == Screen.DASHBOARD) {
                    DashboardView()
                }
                */

            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    WalletUpTheme {
        Greeting("Android")
    }
}

enum class Screen {
    SPLASH,
    DASHBOARD
}