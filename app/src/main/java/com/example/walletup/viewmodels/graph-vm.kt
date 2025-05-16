package com.example.walletup.viewmodels

import androidx.lifecycle.ViewModel
import com.example.walletup.models.TransactionItem
import com.example.walletup.states.BudgetState
import com.example.walletup.states.GraphState
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.util.Calendar
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class GraphViewModel @Inject constructor (

): ViewModel() {
    val db = Firebase.firestore

    private val _state = MutableStateFlow(GraphState())
    val state: StateFlow<GraphState> get() = _state

    init {
        _state.value = GraphState()
    }

    fun getTransacciones() {
        db.collection("Transacciones")
            .get()
            .addOnSuccessListener { result ->
                _state.value?.listaTransacciones?.clear()
                val listaTransacciones = result.map { transaccion ->
                    TransactionItem(
                        id = transaccion.id,
                        idCuenta = transaccion.getString("cuenta") ?: "",
                        cuenta = transaccion.getString("cuenta") ?: "",
                        tipoTransaccion = transaccion.getString("tipo") ?: "",
                        categoria = transaccion.getString("categoria") ?: "",
                        monto = transaccion.getDouble("monto") ?: 0.0,
                        fecha = transaccion.getDate("fecha") ?: Date()
                    )
                }
                _state.value = _state.value.copy(
                    listaTransacciones = ArrayList(listaTransacciones),
                )
                procesarDatosGrafico()
            }
            .addOnFailureListener {

            }
    }

    fun actualizarPeriodo(indice: Int) {
        _state.value = _state.value.copy(
            periodoSeleccionado = indice
        )
        procesarDatosGrafico()
    }

    fun procesarDatosGrafico() {
        val periodo = _state.value.periodoSeleccionado

        when (periodo) {
            0 -> {
                procesarDatosDiarios()
            }
            1 -> {
                procesarDatosMensuales()
            }
            2 -> {
                procesarDatosAnuales()
            }
            else -> {
                procesarDatosDiarios()
            }
        }
    }

    fun procesarDatosDiarios() {
        var fechaFinDiaActual = Date()

        var xList: ArrayList<Number> = arrayListOf()
        var yListIngresos: ArrayList<Number> = arrayListOf()
        var yListGastos: ArrayList<Number> = arrayListOf()

        val calendar = Calendar.getInstance()
        calendar.time = fechaFinDiaActual
        calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(
            Calendar.DATE), 0, 0, 0)
        calendar.add(Calendar.SECOND, -1)
        var fechaInicioDiaActual = calendar.time
        calendar.add(Calendar.SECOND, 1)
        for (i in 7 downTo 1 step 1) {
            if (i == 7) {
                xList.add(calendar.get(Calendar.DATE))
                yListIngresos.add(filterTransactionsBy(fechaInicio = fechaInicioDiaActual, fechaFin = fechaFinDiaActual, tipoTransaccion = "I"))
                yListGastos.add(filterTransactionsBy(fechaInicio = fechaInicioDiaActual, fechaFin = fechaFinDiaActual, tipoTransaccion = "G"))
            } else {
                calendar.time = fechaInicioDiaActual
                calendar.add(Calendar.SECOND, -1)
                fechaFinDiaActual = calendar.time
                calendar.add(Calendar.SECOND, 1)
                calendar.add(Calendar.DATE, -1)
                fechaInicioDiaActual = calendar.time
                xList.add(calendar.get(Calendar.DATE))

                yListIngresos.add(filterTransactionsBy(fechaInicio = fechaInicioDiaActual, fechaFin = fechaFinDiaActual, tipoTransaccion = "I"))
                yListGastos.add(filterTransactionsBy(fechaInicio = fechaInicioDiaActual, fechaFin = fechaFinDiaActual, tipoTransaccion = "G"))
            }
        }

        _state.value = _state.value.copy(
            listaDatosGrafico = mapOf(
                "Ingresos" to yListIngresos.toList(),
                "Gastos" to yListGastos.toList()
            ),
            listaLeyenda = xList
        )
    }

    fun procesarDatosMensuales() {
        var fechaFinDiaActual = Date()

        var xList: ArrayList<Number> = arrayListOf()
        var yListIngresos: ArrayList<Number> = arrayListOf()
        var yListGastos: ArrayList<Number> = arrayListOf()

        val calendar = Calendar.getInstance()
        calendar.time = fechaFinDiaActual
        calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), 1, 0, 0, 0)
        var fechaInicioDiaActual = calendar.time
        for (i in 12 downTo 1 step 1) {
            if (i == 12) {
                xList.add(calendar.get(Calendar.MONTH) + 1)
                yListIngresos.add(filterTransactionsBy(fechaInicio = fechaInicioDiaActual, fechaFin = fechaFinDiaActual, tipoTransaccion = "I"))
                yListGastos.add(filterTransactionsBy(fechaInicio = fechaInicioDiaActual, fechaFin = fechaFinDiaActual, tipoTransaccion = "G"))
            } else {
                calendar.time = fechaInicioDiaActual
                calendar.add(Calendar.SECOND, -1)
                fechaFinDiaActual = calendar.time
                calendar.add(Calendar.SECOND, 1)
                calendar.add(Calendar.MONTH, -1)
                fechaInicioDiaActual = calendar.time
                xList.add(calendar.get(Calendar.MONTH) + 1)

                yListIngresos.add(filterTransactionsBy(fechaInicio = fechaInicioDiaActual, fechaFin = fechaFinDiaActual, tipoTransaccion = "I"))
                yListGastos.add(filterTransactionsBy(fechaInicio = fechaInicioDiaActual, fechaFin = fechaFinDiaActual, tipoTransaccion = "G"))
            }
        }

        _state.value = _state.value.copy(
            listaDatosGrafico = mapOf(
                "Ingresos" to yListIngresos.toList(),
                "Gastos" to yListGastos.toList()
            ),
            listaLeyenda = xList
        )
    }

    fun procesarDatosAnuales() {
        var fechaFinDiaActual = Date()

        var xList: ArrayList<Number> = arrayListOf()
        var yListIngresos: ArrayList<Number> = arrayListOf()
        var yListGastos: ArrayList<Number> = arrayListOf()

        val calendar = Calendar.getInstance()
        calendar.time = fechaFinDiaActual
        calendar.set(calendar.get(Calendar.YEAR), 0, 1, 0, 0, 0)
        var fechaInicioDiaActual = calendar.time
        for (i in 5 downTo 1 step 1) {
            if (i == 5) {
                xList.add(calendar.get(Calendar.YEAR))
                yListIngresos.add(filterTransactionsBy(fechaInicio = fechaInicioDiaActual, fechaFin = fechaFinDiaActual, tipoTransaccion = "I"))
                yListGastos.add(filterTransactionsBy(fechaInicio = fechaInicioDiaActual, fechaFin = fechaFinDiaActual, tipoTransaccion = "G"))
            } else {
                calendar.time = fechaInicioDiaActual
                calendar.add(Calendar.SECOND, -1)
                fechaFinDiaActual = calendar.time
                calendar.add(Calendar.SECOND, 1)
                calendar.add(Calendar.YEAR, -1)
                fechaInicioDiaActual = calendar.time
                xList.add(calendar.get(Calendar.YEAR))

                yListIngresos.add(filterTransactionsBy(fechaInicio = fechaInicioDiaActual, fechaFin = fechaFinDiaActual, tipoTransaccion = "I"))
                yListGastos.add(filterTransactionsBy(fechaInicio = fechaInicioDiaActual, fechaFin = fechaFinDiaActual, tipoTransaccion = "G"))
            }
        }

        _state.value = _state.value.copy(
            listaDatosGrafico = mapOf(
                "Ingresos" to yListIngresos.toList(),
                "Gastos" to yListGastos.toList()
            ),
            listaLeyenda = xList
        )
    }

    fun filterTransactionsBy(fechaInicio: Date, fechaFin: Date, tipoTransaccion: String = "G"): Number {
        val listaFiltrada = _state.value.listaTransacciones.filter {
            it.fecha.before(fechaFin) && it.fecha.after(fechaInicio) && it.tipoTransaccion == tipoTransaccion
        }
        if (listaFiltrada.isNotEmpty()) {
            return listaFiltrada.map { it.monto }.reduce { ac, it -> ac + it }
        } else {
            return 0
        }
    }
}