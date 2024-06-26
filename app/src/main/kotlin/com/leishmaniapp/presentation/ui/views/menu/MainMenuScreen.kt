package com.leishmaniapp.presentation.ui.views.menu

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AccountCircle
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
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
import com.leishmaniapp.domain.disease.Disease
import com.leishmaniapp.domain.disease.MockDotsDisease
import com.leishmaniapp.presentation.ui.composables.MainMenuActionButton
import com.leishmaniapp.presentation.ui.layout.InformationTopBar
import com.leishmaniapp.presentation.ui.layout.LeishmaniappScaffold
import com.leishmaniapp.presentation.ui.theme.LeishmaniappTheme

/**
 * @view A04
 */
@Composable
fun MainMenuScreen(
    disease: Disease,
    onBackButton: () -> Unit,
    onProfileSelect: () -> Unit,
    onStartDiagnosis: () -> Unit,
    onPatientList: () -> Unit,
    onAwaitingDiagnoses: () -> Unit,
    onDatabase: () -> Unit
) {
    LeishmaniappScaffold(
        centered = true,
        backButtonAction = onBackButton,
        actions = {
            IconButton(onClick = onProfileSelect) {
                Icon(
                    imageVector = Icons.Rounded.AccountCircle,
                    contentDescription = stringResource(id = R.string.specialist),
                )
            }
        },
    ) { scaffoldPaddingValues ->

        Column(
            modifier = Modifier.padding(scaffoldPaddingValues),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            InformationTopBar(text = disease.displayName)
            LazyColumn(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically),
                contentPadding = PaddingValues(16.dp),
            ) {

                item {
                    MainMenuActionButton(
                        image = painterResource(id = R.drawable.start_diagnosis_menu_image),
                        label = stringResource(id = R.string.start_diagnosis),
                        onClick = onStartDiagnosis,
                    )
                }

                item {
                    MainMenuActionButton(
                        image = painterResource(id = R.drawable.patients_menu_image),
                        label = stringResource(id = R.string.patients),
                        onClick = onPatientList,
                    )
                }

                item {
                    MainMenuActionButton(
                        image = painterResource(id = R.drawable.awaiting_menu_image),
                        label = stringResource(id = R.string.awaiting),
                        onClick = onAwaitingDiagnoses,
                    )
                }

                item {
                    MainMenuActionButton(
                        image = painterResource(id = R.drawable.export_import_menu_image),
                        label = stringResource(id = R.string.export_import),
                        onClick = onDatabase,
                    )
                }
            }
        }
    }
}


@Composable
@Preview
private fun MainMenuScreenPreview() {
    LeishmaniappTheme {
        MainMenuScreen(
            disease = MockDotsDisease,
            onBackButton = {},
            onProfileSelect = {},
            onStartDiagnosis = {},
            onPatientList = {},
            onAwaitingDiagnoses = {},
            onDatabase = {},
        )
    }
}