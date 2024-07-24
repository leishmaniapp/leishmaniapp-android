package com.leishmaniapp.presentation.ui.dialogs

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.itextpdf.layout.properties.VerticalAlignment
import com.leishmaniapp.R
import com.leishmaniapp.domain.disease.LeishmaniasisGiemsaDisease
import com.leishmaniapp.domain.disease.MockDotsDisease
import com.leishmaniapp.domain.disease.MockSpotsDisease
import com.leishmaniapp.domain.entities.Specialist
import com.leishmaniapp.presentation.ui.theme.LeishmaniappTheme
import com.leishmaniapp.utilities.mock.MockGenerator.mock

/**
 * Show an [AlertDialog] containg the user information and some actions
 * @param onLogout Close the current session without deleting stored credentials
 */
@Composable
fun ProfileAlertDialog(
    modifier: Modifier = Modifier,
    specialist: Specialist,
    onLogout: () -> Unit,
    onDismiss: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Card(modifier = modifier) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(12.dp, Alignment.CenterVertically)
            ) {
                Icon(
                    imageVector = Icons.Filled.AccountCircle,
                    contentDescription = stringResource(id = R.string.specialist)
                )

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = stringResource(id = R.string.specialist),
                        style = MaterialTheme.typography.bodyLarge,
                        textAlign = TextAlign.Center,
                    )
                    Text(
                        text = specialist.name,
                        style = MaterialTheme.typography.displaySmall,
                        textAlign = TextAlign.Center,
                    )
                    Text(text = specialist.email, textAlign = TextAlign.Center)
                }

                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Button(
                        onClick = onLogout,
                    ) {
                        Text(text = stringResource(id = R.string.change))
                    }

                    TextButton(onClick = onDismiss) {
                        Text(text = stringResource(id = R.string.accept))
                    }
                }
            }
        }
    }
}

@Composable
@Preview
private fun ProfileAlertDialogPreview() {
    LeishmaniappTheme {
        ProfileAlertDialog(
            specialist = Specialist.mock()
                .copy(
                    diseases = setOf(
                        MockDotsDisease, MockSpotsDisease, LeishmaniasisGiemsaDisease
                    )
                ),
            onLogout = {},
            onDismiss = {})
    }
}