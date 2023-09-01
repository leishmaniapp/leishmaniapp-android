package com.leishmaniapp.presentation.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import com.leishmaniapp.R
import com.leishmaniapp.presentation.ui.theme.LeishmaniappTheme


@Composable
fun LeishmaniappScaffold(
    title: String = stringResource(id = R.string.app_name),
    /* TODO: Navigation controller as parameter */ backButtonAction: (() -> Unit)? = null,
    content: @Composable BoxScope.() -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                navigationIcon = {
                    if (backButtonAction != null) {
                        IconButton(onClick = backButtonAction) {
                            Icon(
                                Icons.Filled.ArrowBack,
                                contentDescription = stringResource(R.string.back_button),
                            )
                        }
                    }
                },
                title = {
                    Text(
                        text = title, style =
                        MaterialTheme.typography.headlineSmall.copy(
                            fontWeight = if (title == stringResource(id = R.string.app_name)) {
                                FontWeight.Bold
                            } else {
                                FontWeight.Normal
                            }
                        )
                    )
                },
                colors = TopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.onBackground,
                    scrolledContainerColor = MaterialTheme.colorScheme.onBackground,
                    navigationIconContentColor = MaterialTheme.colorScheme.background,
                    titleContentColor = MaterialTheme.colorScheme.background,
                    actionIconContentColor = MaterialTheme.colorScheme.background,
                )
            )
        }
    ) { scaffoldPaddingValues ->
        Box(modifier = Modifier.padding(scaffoldPaddingValues)) {
            content.invoke(this)
        }
    }
}

@Composable
@Preview(heightDp = 200)
fun LeishmaniappScaffoldPreview() {
    LeishmaniappTheme {
        LeishmaniappScaffold {
            Text(text = "Hello world from LeishmaniappScaffold!")
        }
    }
}

@Composable
@Preview(heightDp = 200)
fun LeishmaniappScaffoldPreview_BackButton() {
    LeishmaniappTheme {
        LeishmaniappScaffold(
            title = "This one has a button",
            backButtonAction = {}
        ) {
            Text(text = "Hello world from LeishmaniappScaffold!")
        }
    }
}

@Composable
@Preview(heightDp = 200)
fun LeishmaniappScaffoldPreview_CustomTitle() {
    LeishmaniappTheme {
        LeishmaniappScaffold("This is an scaffold") {
            Text(text = "Hello world from LeishmaniappScaffold!")
        }
    }
}