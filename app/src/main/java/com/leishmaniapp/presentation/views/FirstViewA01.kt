package com.leishmaniapp.presentation.views

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.leishmaniapp.R
import com.leishmaniapp.presentation.ui.LeishmaniappScaffold
import com.leishmaniapp.presentation.ui.theme.LeishmaniappTheme

@Composable
fun firstView(){
    LeishmaniappTheme {
        Surface(modifier = Modifier.fillMaxSize(), color = Color.Black) {
            Column(modifier = Modifier.fillMaxSize()) {
                Spacer(modifier = Modifier.height(50.dp))
                title()
                image()
                description()
                continueB(start = R.string.button_comenzar)
            }
        }
    }
}


@Composable
fun title() {
    val context = LocalContext.current
    Text(
        text = "LeishmaniApp",
        modifier = Modifier
            .fillMaxWidth()
            .height(49.dp),
        style = TextStyle(
            fontSize = 34.sp,
            fontWeight = FontWeight(700),
            color = Color(0xFFFFFFFF),
            textAlign = TextAlign.Center,
            )
    )
}

@Composable
fun image(){
    Image(
        painter = painterResource(R.drawable.image_pantalla_inicial),
        contentDescription = "imagen de pantalla inicial",
        modifier =
        Modifier
            .fillMaxWidth()
            .height(465.dp)
    )
}

@Composable
fun description(){
    Text(
        text = stringResource(id = R.string.description_app),
        style = TextStyle(
            fontSize = 20.sp,
            fontWeight = FontWeight(500),
            color = Color(0xFFFFFFFF),
            textAlign = TextAlign.Center
        ),
        modifier = Modifier
            .fillMaxWidth()
            .height(74.dp)
    )
}

@Preview
@Composable
fun GreetingPreview() {
    LeishmaniappTheme {
        Surface(modifier = Modifier.fillMaxSize(), color = Color.Black) {
            firstView()
        }
    }
}