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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.walletup.R
import com.example.walletup.models.TabItem
import com.example.walletup.models.TransactionItem
import com.example.walletup.viewmodels.DashboardViewModel
import com.patrykandpatrick.vico.compose.cartesian.CartesianChartHost
import com.patrykandpatrick.vico.compose.cartesian.axis.rememberBottom
import com.patrykandpatrick.vico.compose.cartesian.axis.rememberStart
import com.patrykandpatrick.vico.compose.cartesian.layer.rememberLineCartesianLayer
import com.patrykandpatrick.vico.compose.cartesian.rememberCartesianChart
import com.patrykandpatrick.vico.core.cartesian.axis.HorizontalAxis
import com.patrykandpatrick.vico.core.cartesian.axis.VerticalAxis
import com.patrykandpatrick.vico.core.cartesian.data.CartesianChartModelProducer
import com.patrykandpatrick.vico.core.cartesian.data.lineSeries
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date


@Composable

fun DashboardInfoTemplate(
    viewModel: DashboardViewModel,
    onNewTransaction: () -> Unit,
    listaTransacciones: ArrayList<TransactionItem>
){
    val state = viewModel.state.collectAsState()

    LaunchedEffect(state.value.listaTransaccionesFiltrada) {
        viewModel.obtenerInformacionGrafico(state.value.periodoSeleccionado)
    }

    LaunchedEffect(state.value.cuentaSeleccionada) {
        viewModel.obtenerInformacionGrafico(state.value.periodoSeleccionado)
    }

    Column(
        modifier = Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(20.dp))
        UpperSection(
            gastos = state.value.gastos,
            ingresos = state.value.ingresos,
            tipo = state.value.gastoIngresoSeleccionado,
            categoriesDistribution = state.value.categoriesDistribution,
            onNewTransaction = onNewTransaction,
            onTabChange = { indice ->
                viewModel.actualizarInformacionGrafico(indice)
            }
        )
        Spacer(modifier = Modifier.height(40.dp))
        TransactionSection(
            listaTransacciones = listaTransacciones
        )
    }
}

@SuppressLint("DefaultLocale")
@Composable
fun UpperSection(
    gastos: Double = 0.0,
    ingresos: Double = 0.0,
    tipo: Int = 0,
    categoriesDistribution: ArrayList<MontoCategoria> = arrayListOf(),
    onNewTransaction: () -> Unit,
    onTabChange: (Int) -> Unit
) {
    val tabItems = listOf(
        TabItem(
            id = 0,
            titulo = "Día"
        ),
        TabItem(
            id = 1,
            titulo = "Mes"
        ),
        TabItem(
            id = 2,
            titulo = "Año"
        )
    )

    val itemSeleccionado = remember { mutableIntStateOf(0) }

    Column (
        modifier = Modifier
            .shadow(
                elevation = 1.dp,
                shape = RoundedCornerShape(20.dp)
            )
            .background(Color.White)
            .fillMaxWidth(0.9f),
        horizontalAlignment = Alignment.CenterHorizontally,
    ){
        TabBar(
            modifier = Modifier.padding(16.dp),
            items = ArrayList(tabItems),
            idSeleccionado = itemSeleccionado.intValue,
            onClick = { selected ->
                itemSeleccionado.intValue = selected.id
                onTabChange(itemSeleccionado.intValue)
            }
        )
        Text(
            text = formatDate(idSelected = itemSeleccionado.intValue),
            textDecoration = TextDecoration.Underline
        )
        Box(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            InfoGraph(
                modifier = Modifier
                    .align(Alignment.Center),
                categories = categoriesDistribution
            )
            val monto = if (tipo == 0) gastos else ingresos
            Text(
                modifier = Modifier
                    .align(Alignment.Center),
                text = (if (monto<0) "-" else (if (monto>0) "+" else "")) + "$" + String.format("%.2f", monto).replace("-", "")
            )
            Image(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(8.dp)
                    .size(32.dp)
                    .clickable {
                        onNewTransaction()
                    },
                painter = painterResource(R.drawable.ic_add),
                contentDescription = ""
            )
        }
    }
}

@Composable
fun InfoGraph(
    modifier: Modifier,
    categories: ArrayList<MontoCategoria> = arrayListOf()
) {
    var startAngle = -90f
    Canvas(
        modifier = modifier
            .width(200.dp)
            .height(200.dp)
            .padding(30.dp)
            .background(Color.White)
    ) {
        if (categories.isNotEmpty()) {
            categories.forEach {
                val porcentajeActual = it.porcentaje * 360
                val color = when (it.categoria) {
                    "Salud" -> {
                        0xFFFF1A1A
                    }
                    "Transporte" -> {
                        0xFF069F98
                    }
                    "Ocio" -> {
                        0xFF4B069F
                    }
                    "Educación" -> {
                        0xFFC357B5
                    }
                    "Casa" -> {
                        0xFF88D030
                    }
                    "Alimantación" -> {
                        0xFF06559F
                    }
                    "Salario" -> {
                        0xFF1A36FF
                    }
                    "Otros" -> {
                        0xFFFF941A
                    }
                    else -> {
                        0xFF49454F
                    }
                }
                drawArc(
                    color = Color(color),
                    startAngle = startAngle,
                    sweepAngle = porcentajeActual.toFloat(),
                    useCenter = false,
                    style = Stroke(
                        width = 60f,
                        cap = StrokeCap.Square,
                        join = StrokeJoin.Bevel
                    )
                )
                startAngle += porcentajeActual.toFloat()
            }
        } else {
            drawArc(
                color = Color(0xFF5D5D5D),
                startAngle = 0f,
                sweepAngle = 360f,
                useCenter = false,
                style = Stroke(
                    width = 60f,
                    cap = StrokeCap.Square,
                    join = StrokeJoin.Bevel
                )
            )
        }
    }
}


@Composable
fun TabBar(
    modifier: Modifier,
    items: ArrayList<TabItem>,
    idSeleccionado: Int,
    onClick: (TabItem) -> Unit
) {
    Row(
        modifier = modifier
            .background(Color.White)
    ) {
        items.forEach {
            Column(
                modifier = Modifier
                    .clickable {
                        onClick(it)
                    }
                    .background(Color.White)
                    .weight(1.0f/items.size)
            ) {
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = it.titulo,
                    textAlign = TextAlign.Center,
                    color = if (it.id == idSeleccionado) Color.Green else Color.Gray
                )
                Spacer(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(2.dp)
                        .background(if (it.id == idSeleccionado) Color.Green else Color.White)
                )
            }
        }
    }
}

@Composable
fun TransactionSection(
    listaTransacciones: ArrayList<TransactionItem>
) {
    Column(
        modifier = Modifier
            .fillMaxHeight()
            .fillMaxWidth(0.9f)
    ) {
        val movimientos = listaTransacciones

        Text(
            text = "Historia Transaccional",
            fontWeight = FontWeight.SemiBold,
            fontSize = 18.sp,
            color = Color.Black
        )
        Spacer(modifier = Modifier.height(15.dp))
        TransactionList(
            items = java.util.ArrayList(movimientos)
        )
    }
}

@Composable
fun TransactionList(
    items: ArrayList<TransactionItem>
) {
    val scrollState = rememberScrollState(0)
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(scrollState)
    ) {
        items.forEach { transaccion ->
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row {
                    Image(
                        modifier = Modifier
                            .size(24.dp),
                        painter = painterResource(when(transaccion.categoria) {
                            "Salario" -> {
                                R.drawable.salario_icon
                            }
                            "Salud" -> {
                                R.drawable.salud_icon
                            }
                            "Ocio" -> {
                                R.drawable.ocio_icon
                            }
                            "Casa" -> {
                                R.drawable.casa_icon
                            }
                            "Educación" -> {
                                R.drawable.educacion_icon
                            }
                            "Comida" -> {
                                R.drawable.alimentacion_icon
                            }
                            "Otros" -> {
                                R.drawable.otros_icon
                            }
                            else -> {
                                R.drawable.otros_icon
                            }
                        }),
                        contentDescription = ""
                    )
                    Spacer(modifier = Modifier.width(24.dp))
                    Text(
                        text = transaccion.categoria,
                        color = Color(0xFF7E848D)
                    )
                }
                Text(
                    text = "$${transaccion.monto}",
                    color = Color.Black,
                    fontWeight = FontWeight.SemiBold
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

fun formatDate(idSelected: Int): String {
    val currentTime = Calendar.getInstance().time
    val calendar = Calendar.getInstance()
    val dateFormatter = SimpleDateFormat("dd/MM/yyyy", java.util.Locale.US)
    when (idSelected) {
        0 -> {
            return dateFormatter.format(calendar.time)
        }
        1 -> {
            calendar.add(Calendar.DAY_OF_YEAR, -30)
            return "${dateFormatter.format(calendar.time)} - ${dateFormatter.format(currentTime)}"
        }
        2 -> {
            calendar.add(Calendar.DAY_OF_YEAR, -365)
            return "${dateFormatter.format(calendar.time)} - ${dateFormatter.format(currentTime)}"
        }
        else -> {
            return "-"
        }
    }
}


data class MontoCategoria(
    var categoria: String = String(),
    var montoTotal: Double = 0.0,
    var porcentaje: Double = 0.0,
    var tipo: String = "G"
)