package com.leishmaniapp.presentation.views

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
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
import com.leishmaniapp.presentation.theme.LeishmaniappTheme
import com.leishmaniapp.presentation.theme.Purple41

class MenuTypeDeseaseActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent() {
            LeishmaniappTheme {
                Surface(modifier = Modifier.fillMaxSize(), color = Color.Black) {
                    val scrollState = rememberScrollState()
                    Column {
                        Spacer(modifier = Modifier.height(59.dp))
                        titleMenu()
                        Column(modifier = Modifier.verticalScroll(scrollState)) {
                            buttontype(painter = painterResource(id = R.drawable.image_comenzar_diagnostico), R.string.button_start_diagnosis)
                            buttontype(painter = painterResource(id = R.drawable.image_pacientes), R.string.button_pacients)
                            buttontype(painter = painterResource(id = R.drawable.image_pendientes), R.string.button_pacients)
                            buttontype(painter = painterResource(id = R.drawable.image_importa_exporta), R.string.button_import_export)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun titleMenu(){
    Text(
        text = "LeishmaniApp",
        modifier = Modifier
            .fillMaxWidth()
            .height(49.dp),
        style = TextStyle(
            fontSize = 34.sp,
            //fontFamily = FontFamily(Font(R.font.)),
            fontWeight = FontWeight(700),
            color = Color(0xFFFFFFFF),
            textAlign = TextAlign.Center,
        )
    )
}

@Composable
fun buttontype(painter: Painter, stringResId: Int){
    val context = LocalContext.current

    Spacer(modifier = Modifier
        .height(51.dp)
    )
    Box(
        modifier = Modifier.fillMaxWidth()
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 30.dp)
        ) {
            Image(
                painter = painter,
                contentDescription = null,
                contentScale = ContentScale.FillBounds,
                modifier = Modifier
                    .clickable {
                        context.startActivity(Intent(context, earringsActivity::class.java))
                    }
                    .width(343.dp)
                    .height(168.dp)
                    .align(Alignment.Center)
            )
        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 133.dp)
                .align(Alignment.Center)
        ) {
            Text(
                text = stringResource(id = stringResId),
                modifier = Modifier
                    .align(Alignment.Center),
                style = TextStyle(
                    fontSize = 20.sp,
                    //fontFamily = FontFamily(Font(R.font.inter)),
                    fontWeight = FontWeight(500),
                    color = Purple41,
                )
            )
        }
    }
}
