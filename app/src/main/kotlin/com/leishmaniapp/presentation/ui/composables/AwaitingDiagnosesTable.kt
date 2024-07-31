package com.leishmaniapp.presentation.ui.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.leishmaniapp.R
import com.leishmaniapp.presentation.ui.theme.LeishmaniappTheme

@Composable
fun AwaitingDiagnosesTable(
    modifier: Modifier = Modifier,
    contents: ((LazyListScope) -> Unit)?,
) {
    Column(
        modifier = modifier
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.onBackground,
                shape = RoundedCornerShape(16.dp)
            )
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
                .background(
                    color = MaterialTheme.colorScheme.primary,
                    shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = stringResource(id = R.string.awaiting_diagnoses),
                style = MaterialTheme.typography.bodyLarge.copy(color = MaterialTheme.colorScheme.onPrimary)
            )
        }

        LazyColumn {
            // Invoke the contents
            if (contents != null) {
                contents.invoke(this)
            } else {
                item {
                    ListItem(headlineContent = {
                        Text(text = stringResource(id = R.string.no_awaiting_diagnoses))
                    })
                }
            }
        }
    }
}

@Composable
@Preview
fun AwaitingDiagnosesTablePreview() {
    LeishmaniappTheme {
        AwaitingDiagnosesTable {
            it.item {
                ListItem(headlineContent = { Text(text = "Lorem Ipsum") })
            }
        }
    }
}

@Composable
@Preview
fun AwaitingDiagnosesTablePreview_isEmpty() {
    LeishmaniappTheme {
        AwaitingDiagnosesTable(contents = null)
    }
}