package com.leishmaniapp.presentation.views

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import com.leishmaniapp.presentation.ui.MainMenuActionButton
import com.leishmaniapp.presentation.ui.theme.LeishmaniappTheme

/**
 * @view A04
 */
@Composable
fun MainMenuScreen() {
    LeishmaniappTheme {
        Scaffold(
            containerColor = Color.Black
        ) { scaffoldPaddingValues ->
            val scrollState = rememberScrollState()
            Column(modifier = Modifier.padding(scaffoldPaddingValues)) {

                Spacer(modifier = Modifier.height(32.dp))

                Text(
                    text = stringResource(id = R.string.app_name),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 24.dp, horizontal = 16.dp),
                    style = TextStyle(
                        fontSize = 34.sp,
                        fontWeight = FontWeight(700),
                        color = Color.White,
                        textAlign = TextAlign.Center,
                    )
                )

                Column(modifier = Modifier.verticalScroll(scrollState)) {
                    MainMenuActionButton(
                        modifier = Modifier.padding(16.dp),
                        image = painterResource(id = R.drawable.start_diagnosis_menu_image),
                        label = stringResource(id = R.string.button_start_diagnosis)
                    ) {
                        /* TODO: On action click */
                    }

                    MainMenuActionButton(
                        modifier = Modifier.padding(16.dp),
                        image = painterResource(id = R.drawable.patients_menu_image),
                        label = stringResource(id = R.string.button_patients)
                    ) {
                        /* TODO: On action click */
                    }

                    MainMenuActionButton(
                        modifier = Modifier.padding(16.dp),
                        image = painterResource(id = R.drawable.awaiting_menu_image),
                        label = stringResource(id = R.string.button_reminder)
                    ) {
                        /* TODO: On action click */
                    }

                    MainMenuActionButton(
                        modifier = Modifier.padding(16.dp),
                        image = painterResource(id = R.drawable.export_import_menu_image),
                        label = stringResource(id = R.string.export_import)
                    ) {
                        /* TODO: On action click */
                    }

                    Spacer(modifier = Modifier.height(32.dp))
                }
            }
        }
    }
}


@Composable
@Preview
fun MainMenuScreenPreview() {
    LeishmaniappTheme {
        MainMenuScreen()
    }
}