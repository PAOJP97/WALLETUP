package com.example.walletup.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.walletup.models.Cuenta
import com.example.walletup.states.AccountsState
import com.example.walletup.states.TransactionsState
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class TransactionsViewModel @Inject constructor (

): ViewModel() {
    val db = Firebase.firestore

    private val _state = MutableStateFlow(TransactionsState())
    val state: StateFlow<TransactionsState> get() = _state

    init {
        _state.value = TransactionsState()
    }

    fun getCuentas() {
        db.collection("Cuentas")
            .get()
            .addOnSuccessListener { result ->
                _state.value.listaCuentas.clear()
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
                Log.i("Firebase", "Lista cuentas ${listaCuentas.size}")
            }
            .addOnFailureListener {
                Log.e("Firebase", "Error al obtener cuentas ${it}")
            }
    }

    fun setTipoTransaccion(tipo: String) {
        _state.value = _state.value.copy(
            tipoTransaccion = tipo
        )
    }

    fun setMontoTransaccion(monto: Double) {
        _state.value = _state.value.copy(
            montoTransaccion = monto
        )
    }

    fun setCuentaTransaccion(cuenta: String) {
        _state.value = _state.value.copy(
            cuentaTransaccion = cuenta
        )
    }

    fun setCategoriaTransaccion(categoria: String) {
        _state.value = _state.value.copy(
            categoriaTransaccion = categoria
        )
    }

    fun setFechaTransaccion(fecha: Date) {
        _state.value = _state.value.copy(
            fechaTransaccion = fecha
        )
    }

    fun agregarTransaccion(exito: () -> Unit) {
        val transaccion: HashMap<String, Any> = hashMapOf(
            "cuenta" to (_state.value.cuentaTransaccion),
            "monto" to (_state.value.montoTransaccion),
            "tipo" to (_state.value.tipoTransaccion),
            "categoria" to (_state.value.categoriaTransaccion),
            "fecha" to (_state.value.fechaTransaccion)
        )

        db.collection("Transacciones")
            .add(transaccion)
            .addOnSuccessListener {
                if (_state.value.tipoTransaccion == "G") {
                    actualizarPresupuesto(exito)
                }
                exito()
            }
            .addOnFailureListener {

            }
    }

    fun actualizarPresupuesto(exito: () -> Unit) {
        val monto = _state.value.montoTransaccion

        db.collection("Presupuestos")
            .get()
            .addOnSuccessListener {
                for (presupuesto in it) {
                    val presupuestoId = presupuesto.id
                    val fechaPresupuesto = presupuesto.getDate("fechaFin")
                    val gastosPresupuesto = presupuesto.getDouble("gastos") ?: 0.0
                    if (fechaPresupuesto?.after(Date()) == true) {
                        db.collection("Presupuestos")
                            .document(presupuestoId)
                            .update("gastos", gastosPresupuesto + monto)
                            .addOnSuccessListener {
                                exito()
                            }
                            .addOnFailureListener {

                            }
                    }
                }
            }
            .addOnFailureListener {

            }
    }
}