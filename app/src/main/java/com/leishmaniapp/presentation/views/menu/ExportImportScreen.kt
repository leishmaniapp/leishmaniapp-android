package com.leishmaniapp.presentation.views.menu

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.leishmaniapp.R
import com.leishmaniapp.presentation.ui.LeishmaniappScaffold
import com.leishmaniapp.presentation.ui.theme.LeishmaniappTheme

/**
 * @view F01
 */
@Composable
fun ExportImportScreen() {
    LeishmaniappScaffold(title = stringResource(id = R.string.database)) {
        Column(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .padding(bottom = 0.dp, top = 20.dp)
        ) {
            Text(
                text = stringResource(id = R.string.export_import),
                style = MaterialTheme.typography.headlineMedium
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(32.dp)
                    .weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Button(onClick = { /* TODO: Export database btn */ }) {
                    Text(
                        text = String.format(
                            "%s %s",
                            stringResource(id = R.string.export_db),
                            stringResource(id = R.string.database)
                        )
                    )
                }
                Button(onClick = { /* TODO: Import database btn */ }) {
                    Text(
                        text = String.format(
                            "%s %s",
                            stringResource(id = R.string.import_db),
                            stringResource(id = R.string.database)
                        )
                    )
                }
            }

            Column(
                modifier = Modifier
                    .padding(36.dp)
                    .weight(1f)
            ) {
                Text(text = stringResource(id = R.string.export_import_screen_alert))
            }
        }
    }
}

@Composable
@Preview
fun ExportImportScreenPreview() {
    LeishmaniappTheme {
        ExportImportScreen()
    }
}