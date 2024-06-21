package com.leishmaniapp.presentation.ui.views.menu

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
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
import com.leishmaniapp.entities.disease.Disease
import com.leishmaniapp.entities.disease.MockDotsDisease
import com.leishmaniapp.presentation.ui.composables.MainMenuActionButton
import com.leishmaniapp.presentation.ui.theme.LeishmaniappTheme

/**
 * @view A04
 */
@Composable
fun MainMenuScreen(
    disease: Disease,
    onStartDiagnosis: () -> Unit,
    onPatientList: () -> Unit,
    onAwaitingDiagnoses: () -> Unit,
    onDatabase: () -> Unit
) {
    LeishmaniappTheme {
        Scaffold(
            containerColor = Color.Black
        ) { scaffoldPaddingValues ->
            Column(modifier = Modifier.padding(scaffoldPaddingValues)) {

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(MaterialTheme.colorScheme.primary),
                ) {
                    Text(
                        text = disease.displayName,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        style = MaterialTheme.typography.bodyLarge.copy(
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                    )
                }

                LazyColumn {
                    item {
                        Spacer(modifier = Modifier.height(24.dp))
                    }

                    item {
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
                    }

                    item {
                        MainMenuActionButton(
                            modifier = Modifier.padding(16.dp),
                            image = painterResource(id = R.drawable.start_diagnosis_menu_image),
                            label = stringResource(id = R.string.start_diagnosis),
                            onClick = onStartDiagnosis,
                        )
                    }

                    item {
                        MainMenuActionButton(
                            modifier = Modifier.padding(16.dp),
                            image = painterResource(id = R.drawable.patients_menu_image),
                            label = stringResource(id = R.string.patients),
                            onClick = onPatientList,
                        )
                    }

                    item {
                        MainMenuActionButton(
                            modifier = Modifier.padding(16.dp),
                            image = painterResource(id = R.drawable.awaiting_menu_image),
                            label = stringResource(id = R.string.awaiting),
                            onClick = onAwaitingDiagnoses,
                        )
                    }

                    item {
                        MainMenuActionButton(
                            modifier = Modifier.padding(16.dp),
                            image = painterResource(id = R.drawable.export_import_menu_image),
                            label = stringResource(id = R.string.export_import),
                            onClick = onDatabase,
                        )
                    }

                    item {
                        Spacer(modifier = Modifier.height(32.dp))
                    }
                }
            }
        }
    }
}


@Composable
@Preview
fun MainMenuScreenPreview() {
    LeishmaniappTheme {
        MainMenuScreen(
            disease = MockDotsDisease,
            onStartDiagnosis = {},
            onPatientList = {},
            onAwaitingDiagnoses = {},
            onDatabase = {},
        )
    }
}