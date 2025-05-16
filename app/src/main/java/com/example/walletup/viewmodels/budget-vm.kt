package com.example.walletup.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.walletup.models.Presupuesto
import com.example.walletup.states.BudgetState
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.util.Calendar
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class BudgetViewModel @Inject constructor (

): ViewModel() {
    val db = Firebase.firestore

    private val _state = MutableStateFlow(BudgetState())
    val state: StateFlow<BudgetState> get() = _state

    init {
        _state.value = BudgetState()
    }

    fun getPresupuestos() {
        db.collection("Presupuestos")
            .get()
            .addOnSuccessListener { result ->
                _state.value.listaPresupuestos.clear()
                val listaPresupuestos = result.map { cuenta ->
                    Presupuesto(
                        id = cuenta.id,
                        nombre = cuenta.getString("nombre") ?: "",
                        monto = cuenta.getDouble("monto") ?: 0.0,
                        gastos = cuenta.getDouble("gastos") ?: 0.0
                    )
                }
                _state.value = _state.value.copy(
                    listaPresupuestos = ArrayList(listaPresupuestos)
                )
                Log.i("Firebase", "Lista cuentas ${listaPresupuestos.size}")
            }
            .addOnFailureListener {
                Log.e("Firebase", "Error al obtener cuentas ${it}")
            }
    }

    fun setNombrePresupuesto(nombre: String) {
        _state.value = _state.value.copy(
            nombrePresupuesto = nombre
        )
    }

    fun setMontoPresupuesto(monto: Double) {
        _state.value = _state.value.copy(
            montoPresupuesto = monto
        )
    }

    fun agregarPresupuesto(exito: () -> Unit) {
        val calendar = Calendar.getInstance()
        calendar.time = Date()
        calendar.add(Calendar.DATE, 30)
        val fechaFin = calendar.time

        val presupuesto: HashMap<String, Any> = hashMapOf(
            "nombre" to (_state.value.nombrePresupuesto),
            "monto" to (_state.value.montoPresupuesto),
            "gastos" to 0.0,
            "fechaFin" to fechaFin
        )

        db.collection("Presupuestos")
            .add(presupuesto)
            .addOnSuccessListener {
                exito()
            }
            .addOnFailureListener {

            }
    }

    fun actualizarMonto(idPresupuesto: String, monto: Double, exito: () -> Unit) {
        db.collection("Presupuestos")
            .document(idPresupuesto)
            .update("monto", monto)
            .addOnSuccessListener {
                exito()
            }
            .addOnFailureListener {

            }
    }
}