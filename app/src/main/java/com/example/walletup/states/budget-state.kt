package com.example.walletup.states

import com.example.walletup.models.Presupuesto

data class BudgetState(
    val listaPresupuestos: ArrayList<Presupuesto> = arrayListOf(),
    val idPresupuestoModificacion: String = String(),
    val nombrePresupuesto: String = String(),
    val montoPresupuesto: Double = 0.0
)