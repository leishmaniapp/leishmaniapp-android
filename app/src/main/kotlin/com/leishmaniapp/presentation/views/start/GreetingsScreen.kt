package com.leishmaniapp.presentation.views.start

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.leishmaniapp.R
import com.leishmaniapp.presentation.ui.theme.LeishmaniappTheme

/**
 * @view A01
 */
@Composable
fun GreetingsScreen(onContinue: () -> Unit) {
    LeishmaniappTheme {
        Surface(modifier = Modifier.fillMaxSize(), color = Color.Black) {
            /* Image is shown in background */
            Image(
                painter = painterResource(R.drawable.greetings_screen_background),
                contentDescription = null,
                modifier = Modifier.fillMaxWidth()
            )

            /* Foreground content */
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 32.dp, vertical = 64.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = stringResource(id = R.string.app_name),
                    modifier = Modifier.weight(1f),
                    style = TextStyle(
                        fontSize = 34.sp,
                        fontWeight = FontWeight(700),
                        color = Color.White,
                        textAlign = TextAlign.Center,
                    )
                )

                Text(
                    text = stringResource(id = R.string.app_description),
                    style = MaterialTheme.typography.bodyLarge.copy(color = Color.White),
                    textAlign = TextAlign.Center
                )

                Button(
                    modifier = Modifier
                        .padding(vertical = 16.dp, horizontal = 64.dp)
                        .fillMaxWidth(),
                    onClick = onContinue
                ) {
                    Text(
                        text = stringResource(id = R.string.start),
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun GreetingsScreenPreview() {
    LeishmaniappTheme {
        GreetingsScreen {

        }
    }
}