package com.leishmaniapp.presentation.ui

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun reusableButton (titleButton:String /* metodo onclick*/){
    Button(
        onClick = {

        },
        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.surfaceTint),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 86.dp)
    ){
        Text(
            text = titleButton,
            style = TextStyle(
                fontSize = 20.sp,
                //fontFamily = FontFamily(Font(R.font.inter)),
                fontWeight = FontWeight(500),
                color = MaterialTheme.colorScheme.onSecondary,
                textAlign = TextAlign.Center,
            )
        )
    }
}

@Preview
@Composable
fun reusableButtonPreview(){
    reusableButton(titleButton = "hola")
}