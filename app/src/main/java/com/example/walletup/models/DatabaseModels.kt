package com.example.walletup.models

import java.util.Date

data class Cuenta(
    var id: String,
    var numero: String,
    var nombre: String,
    var saldo: Double = 0.0
)

data class Categoria(
    var id: Int,
    var nombre: String,
    var icono: Int
)

data class Presupuesto(
    var id: String,
    var nombre: String,
    var monto: Double,
    var gastos: Double
)

data class TabItem(
    var id: Int,
    var titulo: String
)

data class TransactionItem(
    var id: String,
    var idCuenta: String,
    var cuenta: String,
    var tipoTransaccion: String,
    var categoria: String,
    var monto: Double,
    var fecha: Date
)