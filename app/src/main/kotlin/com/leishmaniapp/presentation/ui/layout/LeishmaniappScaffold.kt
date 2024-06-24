package com.leishmaniapp.presentation.ui.layout

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Help
import androidx.compose.material.icons.rounded.BackHand
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.CenterAlignedTopAppBar
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.leishmaniapp.R
import com.leishmaniapp.presentation.ui.theme.LeishmaniappTheme


@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun LeishmaniappScaffold(
    title: String = stringResource(id = R.string.app_name),
    centered: Boolean = false,
    backButtonAction: (() -> Unit)? = null,
    actions: @Composable RowScope.() -> Unit = {},
    bottomBar: @Composable () -> Unit = {},
    content: @Composable (PaddingValues) -> Unit
) {
    Scaffold(
        topBar = {
            if (centered) {
                CenterAlignedTopAppBar(
                    actions = actions,
                    navigationIcon = {
                        if (backButtonAction != null) {
                            IconButton(onClick = backButtonAction) {
                                Icon(
                                    Icons.AutoMirrored.Filled.ArrowBack,
                                    contentDescription = stringResource(R.string.back),
                                )
                            }
                        }
                    },
                    title = {
                        Text(
                            text = title,
                            style =
                            MaterialTheme.typography.headlineSmall.copy(
                                fontWeight = if (title == stringResource(id = R.string.app_name)) {
                                    FontWeight.Bold
                                } else {
                                    FontWeight.Normal
                                },
                            ),
                        )
                    },
                    colors = TopAppBarColors(
                        containerColor = MaterialTheme.colorScheme.onBackground,
                        scrolledContainerColor = MaterialTheme.colorScheme.onBackground,
                        navigationIconContentColor = MaterialTheme.colorScheme.background,
                        titleContentColor = MaterialTheme.colorScheme.background,
                        actionIconContentColor = MaterialTheme.colorScheme.background,
                    ),
                )
            } else {
                TopAppBar(
                    actions = actions,
                    navigationIcon = {
                        if (backButtonAction != null) {
                            IconButton(onClick = backButtonAction) {
                                Icon(
                                    Icons.AutoMirrored.Filled.ArrowBack,
                                    contentDescription = stringResource(R.string.back),
                                )
                            }
                        }
                    },
                    title = {
                        Text(
                            text = title,
                            style =
                            MaterialTheme.typography.headlineSmall.copy(
                                fontWeight = if (title == stringResource(id = R.string.app_name)) {
                                    FontWeight.Bold
                                } else {
                                    FontWeight.Normal
                                },
                            ),
                        )
                    },
                    colors = TopAppBarColors(
                        containerColor = MaterialTheme.colorScheme.onBackground,
                        scrolledContainerColor = MaterialTheme.colorScheme.onBackground,
                        navigationIconContentColor = MaterialTheme.colorScheme.background,
                        titleContentColor = MaterialTheme.colorScheme.background,
                        actionIconContentColor = MaterialTheme.colorScheme.background,
                    ),
                )
            }
        },
        bottomBar = bottomBar
    ) { scaffoldPaddingValues ->
        content.invoke(scaffoldPaddingValues)
    }
}

@Composable
@Preview(heightDp = 150)
fun LeishmaniappScaffoldPreview() {
    LeishmaniappTheme {
        LeishmaniappScaffold {
            Box(modifier = Modifier.padding(it)) {
                Text(text = "Hello world from LeishmaniappScaffold!")
            }
        }
    }
}

@Composable
@Preview(heightDp = 150)
fun LeishmaniappScaffoldPreview_BackButtonAndActions() {
    LeishmaniappTheme {
        LeishmaniappScaffold(
            title = "Back Button and Actions",
            backButtonAction = {},
            actions = {
                IconButton(onClick = { }) {
                    Icon(imageVector = Icons.Rounded.BackHand, contentDescription = null)
                }
            }
        ) {
            Box(modifier = Modifier.padding(it)) {
                Text(text = "Hello world from LeishmaniappScaffold!")
            }
        }
    }
}

@Composable
@Preview(heightDp = 150)
fun LeishmaniappScaffoldPreview_BackButtonAndActionsCentered() {
    LeishmaniappTheme {
        LeishmaniappScaffold(
            centered = true,
            title = "Centered TopBar",
            backButtonAction = {},
            actions = {
                IconButton(onClick = { }) {
                    Icon(imageVector = Icons.Rounded.BackHand, contentDescription = null)
                }
            }
        ) {
            Box(modifier = Modifier.padding(it)) {
                Text(text = "Hello world from LeishmaniappScaffold!")
            }
        }
    }
}

@Composable
@Preview(heightDp = 200)
fun LeishmaniappScaffoldPreview_WithBottomBar() {
    LeishmaniappTheme {
        LeishmaniappScaffold(bottomBar = {
            BottomAppBar {
                Text(text = "This is a BottomAppBar")
            }
        }) {
            Box(modifier = Modifier.padding(it)) {
                Text(text = "Hello world from LeishmaniappScaffold!")
            }
        }
    }
}