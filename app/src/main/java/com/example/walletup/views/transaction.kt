package com.example.walletup.views

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.walletup.R
import com.example.walletup.viewmodels.TransactionsViewModel

@Composable
fun TransactionView(
    tipoTransaccion: String = "G",
    onAddTransaction: () -> Unit,
    onBack: () -> Unit
) {
    val viewModel: TransactionsViewModel = hiltViewModel()

    val state = viewModel.state.collectAsState()

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
                            text = "Nueva transaccion",
                            fontSize = 18.sp
                        )
                    }
                }
            }
        },
        screenContent = {
            TransactionFormTemplate(
                tipoTransaccion = tipoTransaccion,
                viewModel = viewModel,
                onAddTransaction = onAddTransaction
            )
        }
    )
}