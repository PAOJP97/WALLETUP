package com.example.walletup.views

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun DashboardView (){
    val selected = remember { mutableIntStateOf(0) }

    ContentTemplate(topBarContent = {

        Spacer(modifier = Modifier.height(150.dp))
        Row {
            Text(
                modifier = Modifier
                    .weight(0.5f)
                    .clickable {
                        selected.intValue = 0
                    },
                text = "GASTOS"
            )
            Text(
                modifier = Modifier
                    .weight(0.5f)
                    .clickable {
                        selected.intValue = 1
                    },
                text = "INGRESOS"
            )
        }
    }, screenContent = {
        if (selected.intValue == 0) {
            Text(
                text = "DESDE GASTOS"
            )
            DashboardInfoTemplate()
        } else {
            Text(
                text = "DESDE INGRESOS"
            )
            DashboardInfoTemplate()
        }
    })



}
