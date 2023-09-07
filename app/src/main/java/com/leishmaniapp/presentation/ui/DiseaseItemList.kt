package com.leishmaniapp.presentation.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.leishmaniapp.R
import com.leishmaniapp.presentation.ui.theme.LeishmaniappTheme

@Composable
fun DiseaseItemList(
    modifier: Modifier = Modifier, image: Painter, label: String, onClick: () -> Unit
) {
    Column(modifier = modifier.clickable { onClick.invoke() }) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                modifier = Modifier.weight(1f),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = image,
                    contentDescription = null,
                    modifier = modifier
                        .size(65.dp)
                        .clip(CircleShape),
                    contentScale = ContentScale.FillBounds
                )

                Text(
                    text = label,
                    modifier = Modifier.padding(16.dp)
                )
            }

            Icon(
                Icons.Default.KeyboardArrowRight,
                contentDescription = null,
                modifier = Modifier.align(Alignment.CenterVertically)
            )
        }

        HorizontalDivider(
            color = MaterialTheme.colorScheme.surfaceVariant
        )
    }
}

@Composable
@Preview(showBackground = true)
fun DiseaseItemListPreview() {
    LeishmaniappTheme {
        DiseaseItemList(
            image = painterResource(id = R.drawable.image_example),
            label = stringResource(id = R.string.label_leishmaniasis)
        ) {

        }
    }
}