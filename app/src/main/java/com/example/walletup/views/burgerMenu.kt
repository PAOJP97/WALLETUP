package com.example.walletup.views

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.walletup.MainActivity
import com.example.walletup.R
import com.example.walletup.viewmodels.BurgerMenuViewModel

@Composable
fun BurgerMenuView(
    onBack: () -> Unit,
    onCuentas: () -> Unit,
    onGraficos: () -> Unit,
    onPresupuestos: () -> Unit
) {
    val viewModel: BurgerMenuViewModel = hiltViewModel()

    ContentTemplate(
        topBarContent = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        painter = painterResource(R.drawable.backbutton),
                        contentDescription = "",
                        modifier = Modifier
                            .size(40.dp)
                            .clickable {
                                onBack()
                            }
                    )
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Menu principal",
                            fontSize = 18.sp
                        )
                    }
                }
            }
        },
        screenContent = {
            Column(
                modifier = Modifier
                    .padding(20.dp)
            ) {
                Menu(
                    icono = R.drawable.cuentas_icon,
                    titulo = "Cuentas",
                    action = {
                        onCuentas()
                    },
                    esToggle = false
                )
                Spacer(modifier = Modifier.height(10.dp))
                Menu(
                    icono = R.drawable.chart_icon,
                    titulo = "Graficos",
                    action = {
                        onGraficos()
                    },
                    esToggle = false
                )
                Spacer(modifier = Modifier.height(10.dp))
                Menu(
                    icono = R.drawable.budget_icon,
                    titulo = "Presupuestos",
                    action = {
                        onPresupuestos()
                    },
                    esToggle = false
                )
                Spacer(modifier = Modifier.height(10.dp))
                Menu(
                    icono = R.drawable.notificacion_icon,
                    titulo = "Notificaciones",
                    action = {
                        viewModel.changeAllowNotifications(it)
                    },
                    esToggle = true
                )
                Spacer(modifier = Modifier.height(10.dp))
                Menu(
                    icono = R.drawable.widget_icon,
                    titulo = "Widget",
                    action = {
                        viewModel.changeAllowWidget(it)
                    },
                    esToggle = true
                )
            }
        }
    )
}


@Composable
fun Menu(
    icono: Int,
    titulo: String,
    action: (Boolean) -> Unit,
    esToggle: Boolean
) {
    val checked = remember { mutableStateOf(if (titulo == "Notificaciones") MainActivity.allowNotifications else MainActivity.allowWidget) }

    Row(
        modifier = Modifier
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(id = icono),
                contentDescription = "",
                tint = Color(0xFFA2A2A7),
                modifier = Modifier
                    .size(32.dp)
            )
            Spacer(modifier = Modifier.width(10.dp))
            Text(
                text = titulo
            )
        }
        if (esToggle) {
            Switch(
                checked = checked.value,
                onCheckedChange = {
                    checked.value = it
                    action(it)
                }
            )
        } else {
            Image(
                painter = painterResource(id = R.drawable.ic_arrow_icon),
                contentDescription = "",
                modifier = Modifier
                    .size(30.dp)
                    .clickable {
                        action(true)
                    }
            )
        }
    }
}