package com.leishmaniapp

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.leishmaniapp.presentation.ui.theme.LeishmaniappTheme
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.leishmaniapp.presentation.ui.theme.Purple41
import com.leishmaniapp.presentation.views.SignInActivity

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LeishmaniappTheme {
                Surface(modifier = Modifier.fillMaxSize(), color = Color.Black) {
                    Greeting()
                }
            }
        }
    }
}
@Composable
fun Greeting(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    Column(modifier = Modifier.fillMaxSize()) {
        Spacer(modifier = Modifier.height(50.dp))

        Text(
            text = "LeishmaniApp",
            modifier = Modifier
                .fillMaxWidth()
                .height(49.dp),
            style = androidx.compose.ui.text.TextStyle(
                fontSize = 34.sp,
                //fontFamily = FontFamily(Font(R.font.)),
                fontWeight = FontWeight(700),
                color = Color(0xFFFFFFFF),
                textAlign = TextAlign.Center,
            )
        )

        Image(
            painter = painterResource(R.drawable.image_pantalla_inicial),
            contentDescription = "imagen de pantalla inicial",
            modifier =
            Modifier
                .fillMaxWidth()
                .height(465.dp)
        )

        Text(
            text = stringResource(id = R.string.description_app),
            style = androidx.compose.ui.text.TextStyle(
                fontSize = 20.sp,
                fontWeight = FontWeight(500),
                color = Color(0xFFFFFFFF)
            ),
            modifier = Modifier
                .width(270.dp)
                .height(74.dp)
                .align(Alignment.CenterHorizontally)
        )

        Button(
            onClick = {
                context.startActivity(Intent(context, SignInActivity::class.java))
            },
            colors = ButtonDefaults.buttonColors(containerColor = Purple41),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 86.dp)
        ){
            Text(
                text = stringResource(id = R.string.button_comenzar),
                style = androidx.compose.ui.text.TextStyle(
                    fontSize = 20.sp,
                    //fontFamily = FontFamily(Font(R.font.inter)),
                    fontWeight = FontWeight(500),
                    color = Color(0xFFFFFFFF),
                    textAlign = TextAlign.Center,
                )
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    LeishmaniappTheme {
        Greeting()
    }
}