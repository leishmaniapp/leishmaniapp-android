package com.leishmaniapp.presentation.views


import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.leishmaniapp.R
import com.leishmaniapp.presentation.ui.DiseaseItemList
import com.leishmaniapp.presentation.ui.LeishmaniappScaffold
import com.leishmaniapp.presentation.ui.theme.LeishmaniappTheme

/**
 * @view A03
 * @TODO Recieve list of supported diseases dynamically
 */
@Composable
fun DiseasesMenuScreen() {
    LeishmaniappScaffold {
        Column(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .padding(bottom = 0.dp, top = 20.dp)
        ) {
            Text(
                text = stringResource(id = R.string.label_diseases),
                style = MaterialTheme.typography.headlineMedium
            )

            Spacer(modifier = Modifier.height(26.dp))

            var query by remember { mutableStateOf("") }
            var active by remember { mutableStateOf(false) }

            SearchBar(query = query,
                onQueryChange = { query = it },
                onSearch = {
                    active = false/* TODO: On search action */
                },
                active = active,
                onActiveChange = { active = it },
                placeholder = { Text(text = "Buscar enfermedad") },
                leadingIcon = {
                    IconButton(onClick = { /* TODO: Prompt search */ }) {
                        Icon(
                            imageVector = Icons.Default.Search, contentDescription = null
                        )
                    }
                }) {}

            Spacer(modifier = Modifier.height(43.dp))
            DiseaseItemList(
                image = painterResource(id = R.drawable.greetings_screen_background),
                label = stringResource(R.string.label_leishmaniasis),
            ) {
                /* TODO: Click on value */
            }

            DiseaseItemList(
                image = painterResource(id = R.drawable.greetings_screen_background),
                label = stringResource(R.string.label_malaria),
            ) {
                /* TODO: Click on value */
            }
        }
    }
}

@Preview
@Composable
fun DiseasesMenuScreenPreview() {
    LeishmaniappTheme {
        DiseasesMenuScreen()
    }
}