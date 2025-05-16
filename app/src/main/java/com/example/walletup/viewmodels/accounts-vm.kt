package com.example.walletup.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.walletup.models.Cuenta
import com.example.walletup.states.AccountsState
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class AccountsViewModel @Inject constructor (

): ViewModel() {
    val db = Firebase.firestore

    private val _state = MutableStateFlow(AccountsState())
    val state: StateFlow<AccountsState> get() = _state

    init {
        _state.value = AccountsState()
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

    fun setNombreNuevaCuenta(nombre: String) {
        _state.value = _state.value.copy(
            nombreNuevaCuenta = nombre
        )
    }

    fun setSaldoNuevaCuenta(saldo: Double) {
        _state.value = _state.value.copy(
            saldoNuevaCuenta = saldo
        )
    }

    fun agregarCuenta(exito: () -> Unit) {
        val cuenta: HashMap<String, Any> = hashMapOf(
            "nombre" to (_state.value.nombreNuevaCuenta),
            "saldo" to (_state.value.saldoNuevaCuenta),
        )

        db.collection("Cuentas")
            .add(cuenta)
            .addOnSuccessListener {
                exito()
            }
            .addOnFailureListener {

            }
    }
}