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
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.walletup.R
import com.example.walletup.viewmodels.DashboardViewModel

@Composable
fun DashboardView(
    onNewTransaction: (Int) -> Unit,
    onOpenMenu: () -> Unit
){
    val context = LocalContext.current
    val viewModel: DashboardViewModel = hiltViewModel()

    val state = viewModel.state.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.getCuentas()
    }

    ContentTemplate(
        topBarContent = {
            val cuentaSeleccionada = remember { mutableStateOf("") }
            val mostrarDropdown = remember { mutableStateOf(false) }

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
                        painter = painterResource(R.drawable.menu_hamburger),
                        contentDescription = "",
                        modifier = Modifier
                            .size(40.dp)
                            .clickable {
                                onOpenMenu()
                            }
                    )
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "WalletUp",
                            fontSize = 18.sp
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            Box {
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = if (cuentaSeleccionada.value == String()) "Seleccionar una cuenta" else state.value.listaCuentas.filter { it.id == state.value.cuentaSeleccionada }[0].nombre,
                        color = Color.Black,
                        fontSize = 12.sp
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Image(
                        modifier = Modifier
                            .clickable {
                                mostrarDropdown.value = true
                            },
                        painter = painterResource(R.drawable.ic_chevron_down),
                        contentDescription = ""
                    )
                    DropdownMenu(
                        expanded = mostrarDropdown.value,
                        offset = DpOffset(x = (LocalConfiguration.current.screenWidthDp.dp - 80.dp) / 2, y = 0.dp),
                        onDismissRequest = { mostrarDropdown.value = false }
                    ) {
                        state.value.listaCuentas.forEach {
                            DropdownMenuItem(
                                text = {
                                    Text(
                                        text = it.nombre
                                    )
                                },
                                onClick = {
                                    cuentaSeleccionada.value = it.id
                                    viewModel.actualizarCuenta(it.id)
                                    mostrarDropdown.value = false
                                }
                            )
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                modifier = Modifier
                    .fillMaxWidth(),
                text = (if (state.value.saldo<0) "-" else (if (state.value.saldo>0) "+" else "")) + "$${state.value.saldo}".replace("-", ""),
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(16.dp))
            Row {
                Text(
                    modifier = Modifier
                        .weight(0.5f)
                        .clickable {
                            viewModel.actualizarGastoIngreso(0)
                        },
                    text = "GASTOS",
                    textAlign = TextAlign.Center,
                    color = if (state.value.gastoIngresoSeleccionado == 0) Color.White else Color.Black
                )
                Text(
                    modifier = Modifier
                        .weight(0.5f)
                        .clickable {
                            viewModel.actualizarGastoIngreso(1)
                        },
                    text = "INGRESOS",
                    textAlign = TextAlign.Center,
                    color = if (state.value.gastoIngresoSeleccionado == 1) Color.White else Color.Black
                )
            }
        },
        screenContent = {
            DashboardInfoTemplate(
                viewModel = viewModel,
                onNewTransaction = {
                    onNewTransaction(state.value.gastoIngresoSeleccionado)
                },
                listaTransacciones = state.value.listaTransaccionesFiltrada
            )
        }
    )
}
