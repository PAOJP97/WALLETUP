package com.example.walletup.utils

import android.content.Context
import androidx.compose.runtime.rememberCoroutineScope
import androidx.glance.appwidget.GlanceAppWidgetManager
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.walletup.widget.WalletUpWidget
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlin.coroutines.coroutineContext
import androidx.core.content.edit
import com.example.walletup.models.TransactionItem
import java.util.Date

class WidgetWorker(context: Context, params: WorkerParameters) : Worker(context, params) {
    @OptIn(DelicateCoroutinesApi::class)
    override fun doWork(): Result {
        val firestore = FirebaseFirestore.getInstance()
        val prefs = applicationContext.getSharedPreferences("widget_prefs", Context.MODE_PRIVATE)

        firestore.collection("Transacciones").get()
            .addOnSuccessListener { result ->
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
                val ingresosTotales = listaTransacciones.filter { it.tipoTransaccion == "I" }.map { it.monto }
                val ingresos = if (ingresosTotales.isNotEmpty()) ingresosTotales.reduce { ac, it -> ac + it } else 0.0
                val gastosTotales = listaTransacciones.filter { it.tipoTransaccion == "G" }.map { it.monto }
                val gastos = if (gastosTotales.isNotEmpty()) gastosTotales.reduce { ac, it -> ac + it } else 0.0
                val balance = ingresos - gastos
                prefs.edit {
                    putFloat("ingresos_widget", ingresos.toFloat())
                    putFloat("gastos_widget", gastos.toFloat())
                    putFloat("balance_widget", balance.toFloat())
                }
                GlobalScope.launch {
                    // Optionally request widget update
                    GlanceAppWidgetManager(applicationContext).getGlanceIds(WalletUpWidget::class.java)
                        .forEach {
                            WalletUpWidget().update(applicationContext, it)
                        }
                }
            }

        return Result.success()
    }
}