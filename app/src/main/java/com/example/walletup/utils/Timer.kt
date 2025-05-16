package com.example.walletup.utils

import android.content.Context
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

object Timer {
    private var startTime = System.currentTimeMillis()
    private var job: Job? = null

    fun start(segundos: Long = 5, onTimeReached: () -> Unit) {
        job?.cancel()
        startTime = System.currentTimeMillis()

        job = CoroutineScope(Dispatchers.Default).launch {
            while (isActive) {
                val elapsed = System.currentTimeMillis() - startTime
                if (elapsed >= segundos * 1_000) {
                    withContext(Dispatchers.Main) {
                        onTimeReached()
                    }
                    break
                }
                delay(1000)
            }
        }
    }

    fun cancel() {
        job?.cancel()
    }
}
