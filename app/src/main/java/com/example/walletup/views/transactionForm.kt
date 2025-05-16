package com.example.walletup.views

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.walletup.R
import com.example.walletup.components.DatePicker
import com.example.walletup.models.Categoria
import com.example.walletup.models.Cuenta
import com.example.walletup.viewmodels.TransactionsViewModel
import java.util.Date

@Composable
fun TransactionFormTemplate(
    tipoTransaccion: String = "G",
    viewModel: TransactionsViewModel,
    onAddTransaction: () -> Unit
){
    val state = viewModel.state.collectAsState()

    val scrollState = rememberScrollState(0)

    LaunchedEffect(Unit) {
        viewModel.getCuentas()
        viewModel.setTipoTransaccion(tipoTransaccion)
    }

    val listaCuentas = state.value.listaCuentas
    val categoriasIngresos = listOf(
        Categoria(
            id = 1,
            nombre = "Salario",
            icono = R.drawable.salario_icon
        ),
        Categoria(
            id = 2,
            nombre = "Otros",
            icono = R.drawable.otros_icon
        )
    )
    val categoriasGastos = listOf(
        Categoria(
            id = 3,
            nombre = "Salud",
            icono = R.drawable.salud_icon
        ),
        Categoria(
            id = 4,
            nombre = "Ocio",
            icono = R.drawable.ocio_icon
        ),
        Categoria(
            id = 5,
            nombre = "Casa",
            icono = R.drawable.casa_icon
        ),
        Categoria(
            id = 6,
            nombre = "Educación",
            icono = R.drawable.educacion_icon
        ),
        Categoria(
            id = 7,
            nombre = "Comida",
            icono = R.drawable.alimentacion_icon
        ),
        Categoria(
            id = 8,
            nombre = "Otros",
            icono = R.drawable.otros_icon
        )
    )

    val monto = remember { mutableStateOf("0") }
    val cuentaSeleccionada = remember { mutableStateOf("") }
    val categoriaSeleccionada = remember { mutableStateOf(-1) }
    val mostrarDropdown = remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .verticalScroll(scrollState)
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth(0.9f),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = if (state.value.tipoTransaccion == "G") "GASTOS" else "INGRESOS",
                color = Color(0xFF1717C5),
                fontSize = 36.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(40.dp))
            Text(
                text = "USD",
                color = Color(0xFF1717C5),
                fontSize = 36.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row {
                Text(
                    text = "$",
                    color = Color(0xFF1717C5),
                    fontSize = 36.sp,
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
                        val regex = Regex("^\\d{0,20}(\\.\\d{0,2})?$")
                        if (nuevoValor.isEmpty() || nuevoValor.matches(regex)) {
                            monto.value = nuevoValor
                            viewModel.setMontoTransaccion(nuevoValor.toDoubleOrNull() ?: 0.0)
                        }
                    },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Done
                    )
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Spacer(modifier = Modifier.height(1.dp).fillMaxWidth().background(Color.Gray))
            Spacer(modifier = Modifier.height(40.dp))
            Text(
                text = "Cuenta",
                fontWeight = FontWeight.SemiBold,
                fontSize = 18.sp,
                color = Color.Black
            )
            Spacer(modifier = Modifier.height(15.dp))
            Box {
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = if (cuentaSeleccionada.value == String()) "Seleccionar una cuenta" else state.value.listaCuentas.filter { it.id == state.value.cuentaTransaccion }[0].nombre,
                        color = Color(0xFF7E848D),
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
                        listaCuentas.forEach {
                            DropdownMenuItem(
                                text = {
                                    Text(
                                        text = it.nombre
                                    )
                                },
                                onClick = {
                                    cuentaSeleccionada.value = it.id
                                    viewModel.setCuentaTransaccion(it.id)
                                    mostrarDropdown.value = false
                                }
                            )
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(40.dp))
            Text(
                text = "Categorías",
                fontWeight = FontWeight.SemiBold,
                fontSize = 18.sp,
                color = Color.Black
            )
            Spacer(modifier = Modifier.height(15.dp))
            Column (
                modifier = Modifier
                    .shadow(
                        elevation = 1.dp,
                        shape = RoundedCornerShape(20.dp)
                    )
                    .background(Color.White)
                    .padding(16.dp)
                    .fillMaxWidth(0.9f),
                horizontalAlignment = Alignment.CenterHorizontally
            ){
                (if (state.value.tipoTransaccion == "G") categoriasGastos else categoriasIngresos).chunked(3).forEach { rowItems ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        rowItems.forEach {
                            Row(
                                modifier = Modifier
                                    .weight(1f),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Center
                            ) {
                                ConjuntoCategoria(
                                    it,
                                    categoriaSeleccionada.value,
                                    onClick = {
                                        categoriaSeleccionada.value = it.id
                                        viewModel.setCategoriaTransaccion(it.nombre)
                                    }
                                )
                            }
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(20.dp))
            DatePicker(
                onValueChange = { date ->
                    date?.let {
                        viewModel.setFechaTransaccion(it)
                    }
                }
            )
            Spacer(modifier = Modifier.height(20.dp))
            Button(
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonColors(
                    containerColor = Color(0xFF004AAD),
                    contentColor = Color(0xFF004AAD),
                    disabledContentColor = Color.Black,
                    disabledContainerColor = Color.Black
                ),
                onClick = {
                    viewModel.agregarTransaccion {
                        onAddTransaction()
                    }
                }
            ) {
                Text(
                    text = "Añadir",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
            }
            Spacer(modifier = Modifier.height(40.dp))
        }
    }
}

@Composable
fun ConjuntoCategoria(
    categoria: Categoria,
    seleccionado: Int,
    onClick: (Categoria) -> Unit
) {
    Column(
        modifier = Modifier
            .width(60.dp)
            .clickable {
                onClick(categoria)
            },
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            modifier = Modifier
                .size(32.dp)
                .border(if (categoria.id == seleccionado) 1.dp else 0.dp, color = if (categoria.id == seleccionado) Color.Blue else Color.Transparent, RoundedCornerShape(percent = 50)),
            painter = painterResource(categoria.icono),
            contentDescription = ""
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = categoria.nombre,
            fontSize = 11.sp,
            color = if (categoria.id == seleccionado) Color.Blue else Color.Black,
            fontWeight = if (categoria.id == seleccionado) FontWeight.Bold else FontWeight.Normal
        )
    }
}