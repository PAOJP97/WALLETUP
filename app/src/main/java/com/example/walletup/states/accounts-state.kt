package com.example.walletup.states

import com.example.walletup.models.Cuenta

data class AccountsState(
    val listaCuentas: ArrayList<Cuenta> = arrayListOf(),
    val nombreNuevaCuenta: String = String(),
    val saldoNuevaCuenta: Double = 0.0
)