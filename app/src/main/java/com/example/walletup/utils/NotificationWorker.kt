package com.example.walletup.utils

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.walletup.MainActivity
import com.example.walletup.R

class NotificationWorker(
    context: Context,
    workerParameters: WorkerParameters
): Worker(context, workerParameters) {
    override fun doWork(): Result {
        mostrarNotificacion("WALLETUP", MainActivity.mensajeNotificacion)
        return Result.success()
    }

    fun mostrarNotificacion(titulo: String, mensaje: String) {
        val notificationManager = applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val channelId = "AutoCheck_Channel"
        val channel = NotificationChannel(
            channelId,
            "AutoCheck Notificaciones",
            NotificationManager.IMPORTANCE_HIGH
        )
        notificationManager.createNotificationChannel(channel)

        val notification = NotificationCompat.Builder(applicationContext, channelId)
            .setContentText(mensaje)
            .setSmallIcon(R.drawable.logo) // ícono de tu app
            .build()

        notificationManager.notify(1, notification)
    }
}