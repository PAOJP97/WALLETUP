package com.example.walletup.states

import com.example.walletup.models.Cuenta
import com.example.walletup.models.TransactionItem
import com.example.walletup.views.MontoCategoria

data class DashboardState(
    val listaCuentas: ArrayList<Cuenta> = arrayListOf(),
    val listaTransacciones: ArrayList<TransactionItem> = arrayListOf(),
    val listaTransaccionesFiltrada: ArrayList<TransactionItem> = arrayListOf(),
    val gastos: Double = 0.0,
    val ingresos: Double = 0.0,
    val saldo: Double = 0.0,
    val cuentaSeleccionada: String = String(),
    val gastoIngresoSeleccionado: Int = 0, // 0 - Gastos
    val periodoSeleccionado: Int = 0, // 0 - Dia,
    val categoriesDistribution: ArrayList<MontoCategoria> = arrayListOf()
)