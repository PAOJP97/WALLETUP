package com.example.walletup.widget

import android.content.Context
import androidx.compose.material3.Icon
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.provideContent
import androidx.glance.background
import androidx.glance.layout.Column
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.glance.Image
import androidx.glance.ImageProvider
import androidx.glance.appwidget.SizeMode
import androidx.glance.appwidget.cornerRadius
import androidx.glance.layout.Alignment
import androidx.glance.layout.Row
import androidx.glance.layout.Spacer
import androidx.glance.layout.padding
import androidx.glance.layout.width
import androidx.glance.text.Text
import androidx.glance.text.TextStyle
import androidx.glance.color.*
import androidx.glance.layout.fillMaxHeight
import androidx.glance.layout.fillMaxWidth
import androidx.glance.layout.height
import androidx.glance.layout.size
import androidx.glance.text.FontWeight
import com.example.walletup.R

class WalletUpWidget : GlanceAppWidget() {
    override val sizeMode = SizeMode.Exact

    override suspend fun provideGlance(context: Context, id: GlanceId) {
        val prefs = context.getSharedPreferences("widget_prefs", Context.MODE_PRIVATE)
        val ingresos = prefs.getFloat("ingresos_widget", 0f)
        val gastos = prefs.getFloat("gastos_widget", 0f)
        val balance = prefs.getFloat("balance_widget", 0f)

        provideContent {
            Column(
                modifier = GlanceModifier
                    .padding(10.dp)
                    .background(Color(0xFF2F2F31))
                    .cornerRadius(16.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "WALLETUP",
                        style = TextStyle(
                            color = ColorProvider(Color(0xFFFFFFFF), Color(0xFFFFFFFF)),
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium
                        )
                    )
                    Spacer(modifier = GlanceModifier.width(16.dp))
                    Image(
                        modifier = GlanceModifier
                            .size(14.dp),
                        provider = ImageProvider(R.drawable.vector_icon),
                        contentDescription = String()
                    )
                }
                Spacer(modifier = GlanceModifier.height(16.dp))
                Column(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    val color = if (balance > 0) 0xFF88D030 else (if (balance < 0) 0xFFFF1A1A else 0xFFFFFFFF)
                    Text(
                        text = (if (balance > 0) "+ " else (if (balance < 0) "- " else "")) + "$ ${"$balance".format("%.2f").replace("-", "")}",
                        style = TextStyle(
                            color = ColorProvider(Color(color), Color(color)),
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                    )
                    Spacer(modifier = GlanceModifier.height(4.dp))
                    Text(
                        text = "Ingresos: $ ${"$ingresos".format("%.2f")}",
                        style = TextStyle(
                            color = ColorProvider(Color(0xFFFFFFFF), Color(0xFFFFFFFF)),
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium
                        )
                    )
                    Spacer(modifier = GlanceModifier.height(4.dp))
                    Text(
                        text = "Gastos: $ ${"$gastos".format("%.2f")}",
                        style = TextStyle(
                            color = ColorProvider(Color(0xFFFFFFFF), Color(0xFFFFFFFF)),
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium
                        )
                    )
                }
            }
        }
    }
}