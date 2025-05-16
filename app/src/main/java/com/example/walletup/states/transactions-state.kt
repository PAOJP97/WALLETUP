package com.example.walletup.states

import com.example.walletup.models.Cuenta
import java.util.Date

data class TransactionsState(
    val listaCuentas: ArrayList<Cuenta> = arrayListOf(),
    val cuentaTransaccion: String = String(),
    val categoriaTransaccion: String = String(),
    val montoTransaccion: Double = 0.0,
    val fechaTransaccion: Date = Date(),
    val tipoTransaccion: String = String(),
)