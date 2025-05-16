package com.example.walletup.widget

import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetReceiver

class WalletUpReciever : GlanceAppWidgetReceiver() {
    override val glanceAppWidget: GlanceAppWidget = WalletUpWidget()
}