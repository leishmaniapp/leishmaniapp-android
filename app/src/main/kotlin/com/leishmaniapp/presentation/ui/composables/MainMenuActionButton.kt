package com.leishmaniapp.presentation.ui.composables

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.leishmaniapp.R
import com.leishmaniapp.presentation.ui.theme.LeishmaniappTheme

@Composable
fun MainMenuActionButton(
    modifier: Modifier = Modifier,
    image: Painter,
    label: String,
    onClick: () -> Unit
) {
    OutlinedCard(
        modifier = modifier.fillMaxWidth(),
        onClick = onClick
    ) {
        Image(
            painter = image,
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.CenterHorizontally)
        )

        Text(
            text = label,
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.padding(
                vertical = 8.dp,
                horizontal = 16.dp,
            )
        )
    }
}

@Composable
@Preview
fun MainMenuActionButtonPreview() {
    LeishmaniappTheme {
        MainMenuActionButton(
            image = painterResource(id = R.drawable.start_diagnosis_menu_image),
            label = stringResource(id = R.string.start_diagnosis)
        ) {
            /* Here goes the onClick action */
        }
    }
}