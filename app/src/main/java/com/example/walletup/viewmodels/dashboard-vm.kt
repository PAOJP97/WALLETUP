package com.example.walletup.viewmodels

import androidx.lifecycle.ViewModel
import com.example.walletup.MainActivity
import com.example.walletup.models.Cuenta
import com.example.walletup.models.TransactionItem
import com.example.walletup.states.DashboardState
import com.example.walletup.views.MontoCategoria
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.util.Calendar
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor (

): ViewModel() {
    val db = Firebase.firestore

    private val _state = MutableStateFlow(DashboardState())
    val state: StateFlow<DashboardState> get() = _state

    init {
        _state.value = DashboardState()
    }

    var auxiliarMontoCuenta: Double = 0.0

    fun getCuentas() {
        db.collection("Cuentas")
            .get()
            .addOnSuccessListener { result ->
                _state.value.listaCuentas?.clear()
                val listaCuentas = result.map { cuenta ->
                    Cuenta(
                        id = cuenta.id,
                        numero = cuenta.getString("numero") ?: "",
                        nombre = cuenta.getString("nombre") ?: "",
                        saldo = cuenta.getDouble("saldo") ?: 0.0
                    )
                }
                _state.value = _state.value.copy(
                    listaCuentas = ArrayList(listaCuentas)
                )
                getTransacciones()
            }
            .addOnFailureListener {

            }
    }

    fun getTransacciones() {
        db.collection("Transacciones")
            .get()
            .addOnSuccessListener { result ->
                _state.value.listaTransacciones.clear()
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
                    listaTransaccionesFiltrada = ArrayList(listaTransacciones)
                )
                actualizarListaFiltrada()
                obtenerInformacionNotificacion(ArrayList(listaTransacciones))
                obtenerInformacionGrafico(state.value.periodoSeleccionado)
            }
            .addOnFailureListener {
                obtenerInformacionNotificacion(arrayListOf<TransactionItem>())
            }
    }

    fun actualizarGastoIngreso(gastoIngreso: Int) {
        _state.value = _state.value.copy(
            gastoIngresoSeleccionado = gastoIngreso
        )
        actualizarListaFiltrada()
    }

    fun actualizarInformacionGrafico(indice: Int) {
        _state.value = _state.value.copy(
            periodoSeleccionado = indice
        )
        actualizarListaFiltrada()
    }

    fun actualizarCuenta(id: String) {
        _state.value = _state.value.copy(
            cuentaSeleccionada = id
        )
        actualizarListaFiltrada()
    }

    fun actualizarListaFiltrada() {
        _state.value = _state.value.copy(
            listaTransaccionesFiltrada = filtrarPorPeriodo(filtrarPorGastosIngresos(filtrarPorCuenta()))
        )
    }

    fun filtrarPorCuenta(): ArrayList<TransactionItem> {
        val cuentaSeleccionada = _state.value.cuentaSeleccionada
        val lista = _state.value.listaTransacciones
        val listaCuentas = _state.value.listaCuentas
        if (listaCuentas.isNotEmpty()) {
            auxiliarMontoCuenta = if (cuentaSeleccionada == String()) {
                listaCuentas.map { it.saldo }.reduce { ac, it -> ac + it }
            } else {
                listaCuentas.filter { it.id == cuentaSeleccionada }.map { it.saldo }.reduce { ac, it -> ac + it }
            }
        }
        return if (cuentaSeleccionada == String()) lista else ArrayList(lista.filter {
            it.cuenta == cuentaSeleccionada
        })
    }

    fun filtrarPorGastosIngresos(listaFiltrada: ArrayList<TransactionItem>): ArrayList<TransactionItem> {
        val tipoTransaccion = if (_state.value.gastoIngresoSeleccionado == 0) "G" else "I"
        return ArrayList(listaFiltrada.filter {
            it.tipoTransaccion == tipoTransaccion
        })
    }

    fun filtrarPorPeriodo(listaFiltrada: ArrayList<TransactionItem>): ArrayList<TransactionItem> {
        val periodo = _state.value.periodoSeleccionado
        var fechaInicio: Date = Date()
        val fechaFin: Date = Date()

        val diasAtras = when (periodo) {
            0 -> {
                -1
            }
            1 -> {
                -7
            }
            2 -> {
                -30
            }
            else -> {
                -1
            }
        }

        val calendar = Calendar.getInstance()
        calendar.time = fechaInicio
        calendar.add(Calendar.DATE, diasAtras)
        fechaInicio = calendar.time
        return ArrayList(listaFiltrada.filter {
            it.fecha.before(fechaFin) && it.fecha.after(fechaInicio)
        })
    }

    fun obtenerInformacionGrafico(indice: Int) {
        var fechaInicio: Date = Date()
        val fechaFin: Date = Date()

        val diasAtras = when (indice) {
            0 -> {
                -1
            }
            1 -> {
                -7
            }
            2 -> {
                -30
            }
            else -> {
                -1
            }
        }

        val calendar = Calendar.getInstance()
        calendar.time = fechaInicio
        calendar.add(Calendar.DATE, diasAtras)
        fechaInicio = calendar.time

        val lista = if (_state.value.cuentaSeleccionada == String()) _state.value.listaTransacciones else _state.value.listaTransacciones.filter { it.idCuenta == _state.value.cuentaSeleccionada }

        var gastos = obtenerMontoSegun(lista, "G", fechaInicio, fechaFin, true)
        var ingresos = obtenerMontoSegun(lista, "I", fechaInicio, fechaFin, true)

        _state.value = _state.value.copy(
            gastos = gastos,
            ingresos = ingresos,
            saldo = ingresos - gastos,
            saldoGlobal = obtenerMontoSegun(lista, "I", fechaInicio, fechaFin, false) - obtenerMontoSegun(lista, "G", fechaInicio, fechaFin, false)
        )

        obtenerInformacionGraficoVisual()
    }

    fun obtenerMontoSegun(referencia: List<TransactionItem>, tipo: String, fechaInicio: Date, fechaFin: Date, usarFechas: Boolean = false): Double {
        val listaFiltrada = referencia.filter {
            if (usarFechas) {
                it.tipoTransaccion == tipo && it.fecha.before(fechaFin) && it.fecha.after(fechaInicio)
            } else {
                it.tipoTransaccion == tipo
            }
        }
        return if (listaFiltrada.isEmpty()) 0.0 else listaFiltrada.map{ it.monto }.reduce { total, monto -> total + monto}
    }

    fun obtenerInformacionGraficoVisual() {
        val listaCategorias = state.value.listaTransaccionesFiltrada.map {
            it.categoria
        }.toSet()
        val total = if (state.value.listaTransaccionesFiltrada.isNotEmpty()) state.value.listaTransaccionesFiltrada.map { it.monto }.reduce { ac, value -> ac + value } else 1.0
        val categoriasInfo: ArrayList<MontoCategoria> = arrayListOf()
        listaCategorias.forEach { categoria ->
            val sum = if (state.value.listaTransaccionesFiltrada.isNotEmpty()) state.value.listaTransaccionesFiltrada.filter { it.categoria == categoria }.map { it.monto }.reduce { ac, value -> ac + value } else 0.0
            categoriasInfo.add(
                MontoCategoria(
                    montoTotal = sum,
                    porcentaje = sum/total,
                    categoria = categoria,
                    tipo = if (state.value.gastoIngresoSeleccionado == 0) "G" else "I"
                )
            )
        }
        _state.value = _state.value.copy(
            categoriesDistribution = categoriasInfo
        )
    }

    fun obtenerInformacionNotificacion(lista: ArrayList<TransactionItem>) {
        var fechaInicio: Date = Date()
        val fechaFin: Date = Date()

        val calendar = Calendar.getInstance()
        calendar.time = fechaInicio
        calendar.add(Calendar.DATE, -3)
        fechaInicio = calendar.time

        val listaIngresos = lista.filter {
            it.fecha.before(fechaFin) && it.fecha.after(fechaInicio) && it.tipoTransaccion == "I"
        }
        val listaGastos = lista.filter {
            it.fecha.before(fechaFin) && it.fecha.after(fechaInicio) && it.tipoTransaccion == "I"
        }

        if (lista.isEmpty()) {
            MainActivity.mensajeNotificacion = "Aun no tiene transacciones, empiece ahora"
            return
        }

        if (listaIngresos.isEmpty()) {
            MainActivity.mensajeNotificacion = "Ten cuidado, estás gastando mucho tu dinero"
            return
        }
        if (listaGastos.isEmpty()) {
            MainActivity.mensajeNotificacion = "Excelente, sigue así con tu dinero"
            return
        }

        val montoTotalIngresos = listaIngresos.map { it.monto }.reduce { ac, it -> ac + it }
        val montoTotalGastos = listaGastos.map { it.monto }.reduce { ac, it -> ac + it }

        if (montoTotalIngresos > montoTotalGastos) {
            MainActivity.mensajeNotificacion = "Excelente, tienes más ingresos que gastos, sigue así"
        } else if (montoTotalIngresos < montoTotalGastos) {
            MainActivity.mensajeNotificacion = "Ten cuidado, estás gastando mucho más de lo que generas"
        } else {
            MainActivity.mensajeNotificacion = "Tus cuentas están a la par, pero pueden mejorar"

        }
    }
}