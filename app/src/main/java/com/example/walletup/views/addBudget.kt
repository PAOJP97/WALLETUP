package com.example.walletup.views

import androidx.collection.mutableIntSetOf
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
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.walletup.R
import com.example.walletup.viewmodels.BudgetViewModel


@Composable
fun AddBudgetView(
    onAddBudget: () -> Unit,
    onBack: () -> Unit
) {
    val viewModel: BudgetViewModel = hiltViewModel()

    val state = viewModel.state

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
                            text = "Crear presupuesto",
                            fontSize = 18.sp
                        )
                    }
                }
            }
        },
        screenContent = {
            val nombre = remember { mutableStateOf("") }
            val monto = remember { mutableStateOf("0.00") }

            Column(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxHeight()
                            .fillMaxWidth(0.9f),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Column(
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Spacer(modifier = Modifier.height(24.dp))
                            Text(
                                text = "Nombre del Presupuesto",
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 18.sp,
                                color = Color.Black
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Image(
                                    modifier = Modifier
                                        .size(22.dp),
                                    painter = painterResource(R.drawable.credit_cards),
                                    contentDescription = ""
                                )
                                Spacer(modifier = Modifier.width(40.dp))
                                BasicTextField(
                                    value = nombre.value,
                                    textStyle = TextStyle(
                                        color = Color.Black,
                                        fontSize = 36.sp,
                                        fontWeight = FontWeight.Bold
                                    ),
                                    onValueChange = { nuevoValor ->
                                        nombre.value = nuevoValor
                                        viewModel.setNombrePresupuesto(nuevoValor)
                                    },
                                    keyboardOptions = KeyboardOptions(
                                        keyboardType = KeyboardType.Text,
                                        imeAction = ImeAction.Done
                                    )
                                )
                            }
                            Spacer(modifier = Modifier.height(1.dp).fillMaxWidth().background(Color.Gray))
                        }
                        Spacer(modifier = Modifier.height(20.dp))
                        Text(
                            text = "Presupuesto Mensual",
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 18.sp,
                            color = Color.Black
                        )
                        Spacer(modifier = Modifier.height(8.dp))
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
                                        viewModel.setMontoPresupuesto(nuevoValor.toDoubleOrNull() ?: 0.0)
                                    }
                                },
                                keyboardOptions = KeyboardOptions(
                                    keyboardType = KeyboardType.Number,
                                    imeAction = ImeAction.Done
                                )
                            )
                        }
                        Spacer(modifier = Modifier.height(60.dp))
                        Button(
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonColors(
                                containerColor = Color(0xFF004AAD),
                                contentColor = Color(0xFF004AAD),
                                disabledContentColor = Color.Black,
                                disabledContainerColor = Color.Black
                            ),
                            onClick = {
                                viewModel.agregarPresupuesto {
                                    onAddBudget()
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
                    }
                }
            }
        }
    )
}