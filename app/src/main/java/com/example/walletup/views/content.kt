package com.example.walletup.views

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MovableContent
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ModifierLocalBeyondBoundsLayout
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable


fun ContentTemplate (

    topBarContent: @Composable() () -> Unit,
    screenContent: @Composable() () -> Unit

){

    Column (

        modifier = Modifier
            .fillMaxSize(),


    ){

        Column (
            modifier = Modifier
                .fillMaxWidth()
                .background(brush = Brush.verticalGradient(listOf(
                    Color(0xffCAD1FF),
                    Color(0xff004AAD)
                )))



        ){
            Spacer(modifier = Modifier.height(50.dp))
            topBarContent()
            Spacer(modifier = Modifier.height(20.dp))
        }

        screenContent()
    }

}

@Composable
@Preview(showSystemUi = true, showBackground = true)

fun ContentTemplatePreview(){

    ContentTemplate(

        topBarContent = {
            
            Text(

                text = "Hola"
            )

            Text(

                text = "Hola"
            )
            Text(

                text = "Hola"
            )
            Text(

                text = "Hola"
            )



        },
        screenContent = {

            Text(

                text = "Hola"
            )
            Text(

                text = "Hola"
            )
            Text(

                text = "Hola"
            )
            Text(

                text = "Hola"
            )
        }

    )
}