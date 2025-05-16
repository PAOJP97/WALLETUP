package com.example.walletup.views

import android.annotation.SuppressLint
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.walletup.R
import com.example.walletup.models.Presupuesto
import com.example.walletup.viewmodels.BudgetViewModel
import java.util.Date

@Composable
fun BudgetsView(
    onNewBudget: () -> Unit,
    onBack: () -> Unit
) {
    val viewModel: BudgetViewModel = hiltViewModel()

    val state = viewModel.state.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.getPresupuestos()
    }

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
                            text = "Presupuestos",
                            fontSize = 18.sp
                        )
                    }
                }
            }
        },
        screenContent = {
            val mostrarDialogo = remember { mutableStateOf(false) }
            val presupuestoSeleccionado = remember { mutableStateOf(Presupuesto("", "", 0.0, 0.0)) }
            val presupuestos = state.value.listaPresupuestos

            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxHeight()
                        .fillMaxWidth(0.9f)
                ) {
                    Spacer(modifier = Modifier.height(24.dp))
                    BotonCrear {
                        onNewBudget()
                    }
                    Spacer(modifier = Modifier.height(24.dp))
                    Column(
                        modifier = Modifier
                            .fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Column {
                            presupuestos.forEachIndexed { index, it ->
                                PresupuestoItem(
                                    presupuesto = it,
                                    onEditar = {
                                        mostrarDialogo.value = true
                                        presupuestoSeleccionado.value = it
                                    }
                                )
                                if (index != presupuestos.size - 1) {
                                    Spacer(modifier = Modifier.height(16.dp))
                                }
                            }
                        }
                    }
                }
            }

            if (presupuestoSeleccionado.value.nombre != "") {
                EditarMonto(
                    presupuesto = presupuestoSeleccionado.value,
                    mostrar = mostrarDialogo.value,
                    onClose = {
                        mostrarDialogo.value = false
                        presupuestoSeleccionado.value = Presupuesto("", "", 0.0, 0.0)
                    },
                    onUpdateBudget = {
                        viewModel.actualizarMonto(presupuestoSeleccionado.value.id, monto = it.toDoubleOrNull() ?: 0.0) {
                            viewModel.getPresupuestos()
                        }
                        mostrarDialogo.value = false
                        presupuestoSeleccionado.value = Presupuesto("", "", 0.0, 0.0)
                    }
                )
            }
        }
    )
}

@Composable
fun EditarMonto(
    presupuesto: Presupuesto,
    mostrar: Boolean = false,
    onClose: () -> Unit,
    onUpdateBudget: (String) -> Unit
) {
    val monto = remember { mutableStateOf("0") }

    if (mostrar) {
        Dialog(
            onDismissRequest = {},
            properties = DialogProperties(
                dismissOnBackPress = false,
                dismissOnClickOutside = true
            )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Column(
                    modifier = Modifier
                        .shadow(
                            elevation = 1.dp,
                            shape = RoundedCornerShape(20.dp)
                        )
                        .background(Color.White)
                        .fillMaxWidth(0.9f)
                        .padding(20.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth(0.9f)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Image(
                                    painter = painterResource(R.drawable.credit_cards),
                                    contentDescription = "",
                                    modifier = Modifier.size(24.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = presupuesto.nombre,
                                    fontSize = 14.sp
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(
                            modifier = Modifier
                                .fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    val value = presupuesto.monto - presupuesto.gastos
                                    Text(
                                        text = "Restante:"
                                    )
                                    Text(
                                        text = (if (value < 0) "-" else (if (value > 0) "+" else "")) + "$${value}".replace(
                                            "-",
                                            ""
                                        )
                                    )
                                }
                                Spacer(modifier = Modifier.height(4.dp))
                                Spacer(
                                    modifier = Modifier
                                        .height(1.dp)
                                        .background(Color.Black)
                                        .fillMaxWidth()
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Row(
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    modifier = Modifier
                                        .fillMaxWidth(),
                                ) {
                                    Text(
                                        text = "Presupuesto:"
                                    )
                                    Text(
                                        text = "$${presupuesto.monto}"
                                    )
                                }
                                Spacer(modifier = Modifier.height(4.dp))
                                Row(
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    modifier = Modifier
                                        .fillMaxWidth(),
                                ) {
                                    Text(
                                        text = "Gastos:"
                                    )
                                    Text(
                                        text = "$${presupuesto.gastos}"
                                    )
                                }
                            }
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier
                                .fillMaxWidth(),
                        ) {
                            Spacer(modifier = Modifier.height(8.dp))
                            Column {
                                Text(
                                    text = "Nuevo presupuesto",
                                    color = Color(0xFF1717C5),
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                BasicTextField(
                                    value = monto.value,
                                    textStyle = TextStyle(
                                        color = Color(0xFFB6BFFF),
                                        fontSize = 36.sp,
                                        fontWeight = FontWeight.Bold
                                    ),
                                    onValueChange = { nuevoValor ->
                                        monto.value = nuevoValor
                                    }
                                )
                            }
                        }
                    }
                    Spacer(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(8.dp)
                    )
                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Button(
                            onClick = {
                                onClose()
                            }
                        ) {
                            Text(
                                "Regresar"
                            )
                        }
                        Button(
                            onClick = {
                                onUpdateBudget(monto.value)
                            }
                        ) {
                            Text(
                                "Guardar"
                            )
                        }
                    }
                }
            }
        }
    }
}

@SuppressLint("DefaultLocale")
@Composable
fun PresupuestoItem(
    presupuesto: Presupuesto,
    onEditar: (Presupuesto) -> Unit
) {
    Column(
        modifier = Modifier
            .shadow(
                elevation = 1.dp,
                shape = RoundedCornerShape(20.dp)
            )
            .background(Color.White)
            .fillMaxWidth(0.9f)
            .padding(10.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(R.drawable.credit_cards),
                    contentDescription = "",
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = presupuesto.nombre,
                    fontSize = 14.sp
                )
            }
            Image(
                painter = painterResource(R.drawable.ic_arrow_icon),
                contentDescription = "",
                modifier = Modifier
                    .size(24.dp)
                    .clickable {
                        onEditar(presupuesto)
                    }
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            val restante = (1 - presupuesto.gastos/presupuesto.monto)
            Canvas(
                modifier = Modifier
                    .width(80.dp)
                    .height(80.dp)
                    .padding(15.dp)
                    .background(Color.White)
            ) {
                drawArc(
                    color = Color.Gray,
                    startAngle = -90f,
                    sweepAngle = 360f,
                    useCenter = false,
                    style = Stroke(
                        width = 10f,
                        cap = StrokeCap.Square,
                        join = StrokeJoin.Bevel
                    )
                )
                drawArc(
                    color = Color.Yellow,
                    startAngle = -90f,
                    sweepAngle = 360f * restante.toFloat(),
                    useCenter = false,
                    style = Stroke(
                        width = 10f,
                        cap = StrokeCap.Round,
                        join = StrokeJoin.Round
                    )
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
            Column(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    val value = presupuesto.monto - presupuesto.gastos
                    Text(
                        text = "Restante:"
                    )
                    Text(
                        text = (if (value < 0) "-" else (if (value > 0) "+" else "")) + "$${value}".replace(
                            "-",
                            ""
                        )
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
                Spacer(
                    modifier = Modifier
                        .height(1.dp)
                        .background(Color.Black)
                        .fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(4.dp))
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier
                        .fillMaxWidth(),
                ) {
                    Text(
                        text = "Presupuesto:"
                    )
                    Text(
                        text = "$${presupuesto.monto}"
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier
                        .fillMaxWidth(),
                ) {
                    Text(
                        text = "Gastos:"
                    )
                    Text(
                        text = "$${presupuesto.gastos}"
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        Row(
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxWidth(),
        ) {
            Text(
                text = obtenerMensaje(presupuesto.gastos/presupuesto.monto)
            )
        }
    }
}

fun obtenerMensaje(porcentaje: Double): String {
    if (porcentaje < 0.25) {
        return "Vas por buen camino"
    } else if (porcentaje < 0.5) {
        return "Sigue así"
    } else if (porcentaje < 0.75) {
        return "Ten cuidado con tus gastos"
    } else if (porcentaje < 1) {
        return "Te acercas al límite"
    } else if (porcentaje == 1.0) {
        return "Alcancaste el límite"
    } else {
        return "Has sobrepasado tu presupuesto"
    }
}