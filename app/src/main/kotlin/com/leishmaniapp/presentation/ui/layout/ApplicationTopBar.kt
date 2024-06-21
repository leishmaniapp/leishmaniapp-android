package com.leishmaniapp.presentation.ui.layout

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.leishmaniapp.presentation.ui.theme.LeishmaniappTheme

@Composable
fun ReusableTopBar(text: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.primary),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = text,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth(),
            fontWeight = FontWeight.Light,
            color = MaterialTheme.colorScheme.onPrimary
        )
    }

}

@Preview
@Composable
fun ReusableTopBarPreview() {
    LeishmaniappTheme {
        ReusableTopBar(text = "Lorem Ipsum")
    }
}
