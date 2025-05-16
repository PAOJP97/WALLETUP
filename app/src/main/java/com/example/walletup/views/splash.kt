package com.example.walletup.views

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.walletup.R
import com.example.walletup.viewmodels.SplashViewModel


@Composable

fun SplashView (
    onFinish: () -> Unit
){
    val viewModel: SplashViewModel = hiltViewModel()

    LaunchedEffect(Unit) {
        viewModel.loadPreferences()
    }

    Column (
        modifier = Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ){
        Image(
            painter = painterResource(id = R.drawable.logo),
            contentDescription = "",

            modifier = Modifier
                .size(300.dp)
        )
        Text (
            text =  "Tu nueva forma de ahorrar, más fácil que nunca!",

            color = Color(0xff004AAD),
            fontWeight = FontWeight.Bold,
            fontSize = 11.sp,
        )
    }

    LaunchedEffect(Unit) {
        Thread.sleep(3000)
        onFinish()
    }
}

