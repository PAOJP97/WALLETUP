package com.example.walletup.views

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
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.walletup.R
import com.example.walletup.models.Cuenta
import com.example.walletup.viewmodels.AccountsViewModel

@Composable
fun AccountsView(
    onNewAccount: () -> Unit,
    onBack: () -> Unit
) {
    val viewModel: AccountsViewModel = hiltViewModel()

    val state = viewModel.state.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.getCuentas()
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
                            text = "Cuentas"
                        )
                    }
                }
            }
        },
        screenContent = {
            val cuentas = state.value.listaCuentas

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
                        onNewAccount()
                    }
                    Column(
                        modifier = Modifier
                            .fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "Cuentas",
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 18.sp,
                            color = Color.Black
                        )
                        Column {
                            cuentas.forEachIndexed { index, it ->
                                CuentaItem(
                                    cuenta = it
                                )
                                if (index != cuentas.size - 1) {
                                    Spacer(modifier = Modifier.height(16.dp))
                                }
                            }
                        }
                    }
                }
            }
        }
    )
}

@Composable
fun CuentaItem(
    cuenta: Cuenta
){

    Row (
        modifier = Modifier
            .height(40.dp)
            .shadow(
                elevation = 1.dp,
                shape = RoundedCornerShape(20.dp)
            )
            .background(Color.White)
            .padding(horizontal = 16.dp)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ){
        Row {
            Icon(
                painter = painterResource(R.drawable.cuentas_icon),
                tint = Color(0xFFA2A2A7),
                contentDescription = ""
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = cuenta.nombre,
                fontSize = 14.sp
            )
        }
        Text(
            text = "$ ${cuenta.saldo}",
            fontSize = 14.sp
        )
    }
}


@Composable
fun BotonCrear(
    onClick: () -> Unit
) {
    Column(

    ) {
        Text(
            modifier = Modifier
                .clickable {
                    onClick()
                },
            text = "+ CREAR",
            fontSize = 18.sp,
            color = Color(0xFF1717C5),
        )
        Spacer(modifier = Modifier.height(8.dp))
        Spacer(modifier = Modifier
            .fillMaxWidth()
            .height(1.dp)
            .background(Color(0xFFDEE1EF))
        )
    }
}