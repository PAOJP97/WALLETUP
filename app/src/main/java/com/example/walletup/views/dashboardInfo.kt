package com.example.walletup.views

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
import androidx.compose.runtime.mutableIntStateOf
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
import com.example.walletup.R
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date


@Composable

fun DashboardInfoTemplate(){
    Column(
        modifier = Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(20.dp))
        UpperSection()
        Spacer(modifier = Modifier.height(40.dp))
        TransactionSection()
    }
}

@Composable
fun UpperSection() {
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
                    .align(Alignment.Center)
            )
            Text(
                modifier = Modifier
                    .align(Alignment.Center),
                text = "$50"
            )
            Image(
                modifier = Modifier
                    .align(Alignment.BottomEnd),
                painter = painterResource(R.drawable.ic_add),
                contentDescription = ""
            )
        }
    }
}

@Composable
fun InfoGraph(
    modifier: Modifier
) {
    Canvas(
        modifier = modifier
            .width(200.dp)
            .height(200.dp)
            .padding(30.dp)
            .background(Color.White)
    ) {
        drawArc(
            color = Color.Red,
            startAngle = 0f,
            sweepAngle = 360f,
            useCenter = true,
            style = Stroke(
                width = 60f,
                cap = StrokeCap.Round,
                join = StrokeJoin.Round
            )
        )

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
fun TransactionSection() {
    Column(
        modifier = Modifier
            .fillMaxHeight()
            .fillMaxWidth(0.9f)
    ) {
        val movimientos = listOf(
            TransactionItem(
                id = "0",
                cuenta = "",
                categoria = "Otros",
                monto = 5.99,
                fecha = Date()
            ),
            TransactionItem(
                id = "0",
                cuenta = "",
                categoria = "Salud",
                monto = 43.99,
                fecha = Date()
            ),
            TransactionItem(
                id = "0",
                cuenta = "",
                categoria = "Salud",
                monto = 43.99,
                fecha = Date()
            ),
            TransactionItem(
                id = "0",
                cuenta = "",
                categoria = "Salud",
                monto = 43.99,
                fecha = Date()
            ),
            TransactionItem(
                id = "0",
                cuenta = "",
                categoria = "Salud",
                monto = 43.99,
                fecha = Date()
            ),
            TransactionItem(
                id = "0",
                cuenta = "",
                categoria = "Salud",
                monto = 43.99,
                fecha = Date()
            ),
            TransactionItem(
                id = "0",
                cuenta = "",
                categoria = "Salud",
                monto = 43.99,
                fecha = Date()
            ),
            TransactionItem(
                id = "0",
                cuenta = "",
                categoria = "Salud",
                monto = 43.99,
                fecha = Date()
            ),
            TransactionItem(
                id = "0",
                cuenta = "",
                categoria = "Salud",
                monto = 43.99,
                fecha = Date()
            ),
            TransactionItem(
                id = "0",
                cuenta = "",
                categoria = "Salud",
                monto = 43.99,
                fecha = Date()
            ),
            TransactionItem(
                id = "0",
                cuenta = "",
                categoria = "Salud",
                monto = 43.99,
                fecha = Date()
            ),
            TransactionItem(
                id = "0",
                cuenta = "",
                categoria = "Salud",
                monto = 43.99,
                fecha = Date()
            ),
            TransactionItem(
                id = "0",
                cuenta = "",
                categoria = "Salud",
                monto = 43.99,
                fecha = Date()
            ),
            TransactionItem(
                id = "0",
                cuenta = "",
                categoria = "Salud",
                monto = 43.99,
                fecha = Date()
            ),
            TransactionItem(
                id = "0",
                cuenta = "",
                categoria = "Salud",
                monto = 43.99,
                fecha = Date()
            ),
            TransactionItem(
                id = "0",
                cuenta = "",
                categoria = "Salud",
                monto = 43.99,
                fecha = Date()
            ),
            TransactionItem(
                id = "0",
                cuenta = "",
                categoria = "Salud",
                monto = 43.99,
                fecha = Date()
            ),
            TransactionItem(
                id = "0",
                cuenta = "",
                categoria = "Salud",
                monto = 43.99,
                fecha = Date()
            ),
            TransactionItem(
                id = "0",
                cuenta = "",
                categoria = "Salud",
                monto = 43.99,
                fecha = Date()
            ),
            TransactionItem(
                id = "0",
                cuenta = "",
                categoria = "Salud",
                monto = 43.99,
                fecha = Date()
            ),
            TransactionItem(
                id = "0",
                cuenta = "",
                categoria = "Salud",
                monto = 43.99,
                fecha = Date()
            ),
            TransactionItem(
                id = "0",
                cuenta = "",
                categoria = "Salud",
                monto = 43.99,
                fecha = Date()
            ),
            TransactionItem(
                id = "0",
                cuenta = "",
                categoria = "Salud",
                monto = 43.99,
                fecha = Date()
            ),
            TransactionItem(
                id = "0",
                cuenta = "",
                categoria = "Salud",
                monto = 43.99,
                fecha = Date()
            ),
            TransactionItem(
                id = "0",
                cuenta = "",
                categoria = "Salud",
                monto = 43.99,
                fecha = Date()
            ),
            TransactionItem(
                id = "0",
                cuenta = "",
                categoria = "Salud",
                monto = 43.99,
                fecha = Date()
            ),
            TransactionItem(
                id = "0",
                cuenta = "",
                categoria = "Salud",
                monto = 43.99,
                fecha = Date()
            ),
            TransactionItem(
                id = "0",
                cuenta = "",
                categoria = "Salud",
                monto = 43.99,
                fecha = Date()
            ),
            TransactionItem(
                id = "0",
                cuenta = "",
                categoria = "Salud",
                monto = 43.99,
                fecha = Date()
            ),
            TransactionItem(
                id = "0",
                cuenta = "",
                categoria = "Salud",
                monto = 43.99,
                fecha = Date()
            ),
            TransactionItem(
                id = "0",
                cuenta = "",
                categoria = "Salud",
                monto = 43.99,
                fecha = Date()
            ),
            TransactionItem(
                id = "0",
                cuenta = "",
                categoria = "Salud",
                monto = 43.99,
                fecha = Date()
            ),
            TransactionItem(
                id = "0",
                cuenta = "",
                categoria = "Salud",
                monto = 43.99,
                fecha = Date()
            ),
            TransactionItem(
                id = "0",
                cuenta = "",
                categoria = "Salud",
                monto = 43.99,
                fecha = Date()
            ),
            TransactionItem(
                id = "0",
                cuenta = "",
                categoria = "Salud",
                monto = 43.99,
                fecha = Date()
            ),
            TransactionItem(
                id = "0",
                cuenta = "",
                categoria = "Salud",
                monto = 43.99,
                fecha = Date()
            ),
            TransactionItem(
                id = "0",
                cuenta = "",
                categoria = "Salud",
                monto = 43.99,
                fecha = Date()
            ),
            TransactionItem(
                id = "0",
                cuenta = "",
                categoria = "Salud",
                monto = 43.99,
                fecha = Date()
            ),
            TransactionItem(
                id = "0",
                cuenta = "",
                categoria = "Salud",
                monto = 43.99,
                fecha = Date()
            ),
            TransactionItem(
                id = "0",
                cuenta = "",
                categoria = "Salud",
                monto = 43.99,
                fecha = Date()
            ),
            TransactionItem(
                id = "0",
                cuenta = "",
                categoria = "Salud",
                monto = 43.99,
                fecha = Date()
            ),
            TransactionItem(
                id = "0",
                cuenta = "",
                categoria = "Salud",
                monto = 43.99,
                fecha = Date()
            ),
            TransactionItem(
                id = "0",
                cuenta = "",
                categoria = "Salud",
                monto = 43.99,
                fecha = Date()
            ),
            TransactionItem(
                id = "0",
                cuenta = "",
                categoria = "Salud",
                monto = 43.99,
                fecha = Date()
            ),
            TransactionItem(
                id = "0",
                cuenta = "",
                categoria = "Salud",
                monto = 43.99,
                fecha = Date()
            )
        )

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
                        painter = painterResource(R.drawable.ic_add),
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

        }
    }
}

@Composable
@Preview(showSystemUi = true, showBackground = true)
fun ContentDashboardInfo(){
    Column(
        modifier = Modifier
            .background(Color(0xFFFBFBFB))
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        DashboardInfoTemplate()
    }

}

data class TabItem(
    var id: Int,
    var titulo: String
)

data class TransactionItem(
    var id: String,
    var cuenta: String,
    var categoria: String,
    var monto: Double,
    var fecha: Date
)

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