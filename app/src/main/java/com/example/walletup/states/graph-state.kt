package com.example.walletup.states

import com.example.walletup.models.TransactionItem

data class GraphState(
    val listaTransacciones: ArrayList<TransactionItem> = arrayListOf(),
    val periodoSeleccionado: Int = 0,
    val listaDatosGrafico: Map<String, List<Number>> = mapOf(),
    val listaLeyenda: ArrayList<Number> = arrayListOf()
)