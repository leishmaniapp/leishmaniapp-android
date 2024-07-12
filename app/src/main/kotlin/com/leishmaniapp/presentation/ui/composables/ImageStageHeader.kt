package com.leishmaniapp.presentation.ui.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Album
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.leishmaniapp.presentation.ui.theme.LeishmaniappTheme

@Composable
fun ImageStageHeader(
    icon: ImageVector,
    title: String,
    content: String
) {
    Column(
        modifier = Modifier.padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Row {
            Icon(
                icon,
                contentDescription = title,
                modifier = Modifier.padding(end = 8.dp)
            )
            Text(title)
        }
        Text(content, textAlign = TextAlign.Center)
    }
}

@Composable
@Preview
fun ImageStageHeaderPreview() {
    LeishmaniappTheme {
        Surface {
            ImageStageHeader(
                icon = Icons.Filled.Album,
                title = "Hello World!",
                content = "This is the content",
            )
        }
    }
}