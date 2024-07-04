package com.leishmaniapp.presentation.ui.views.start

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.leishmaniapp.R
import com.leishmaniapp.domain.entities.Specialist
import com.leishmaniapp.domain.types.Email
import com.leishmaniapp.domain.types.Password
import com.leishmaniapp.presentation.ui.layout.LeishmaniappScaffold
import com.leishmaniapp.presentation.ui.theme.LeishmaniappTheme
import com.leishmaniapp.utilities.mock.MockGenerator.mock

/**
 * Show login screen
 * @view A02
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AuthenticationOfflineScreen(
    emails: List<Email>,
    onAuthenticate: (Email, Password) -> Unit,
) {

    var email by rememberSaveable { mutableStateOf("") }
    var expandedDropdown by remember { mutableStateOf(false) }

    var password by rememberSaveable { mutableStateOf("") }
    var showPassword by rememberSaveable { mutableStateOf(false) }

    LeishmaniappScaffold(
        title = stringResource(id = R.string.authentication)
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {

            Column(
                modifier = Modifier.padding(horizontal = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = stringResource(id = R.string.authentication_welcome),
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.headlineMedium,
                )

                Card(modifier = Modifier.padding(12.dp)) {
                    Text(
                        modifier = Modifier.padding(16.dp),
                        text = stringResource(id = R.string.authentication_offline)
                    )
                }

                Text(
                    text = stringResource(id = R.string.authentication_content_offline),
                    textAlign = TextAlign.Center,
                )
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 24.dp)
            ) {

                ExposedDropdownMenuBox(
                    modifier = Modifier.fillMaxWidth(),
                    expanded = expandedDropdown,
                    onExpandedChange = {
                        expandedDropdown = !expandedDropdown
                    }) {

                    OutlinedTextField(
                        modifier = Modifier
                            .fillMaxWidth()
                            .menuAnchor(),
                        value = email,
                        readOnly = true,
                        onValueChange = {},
                        label = { Text(text = stringResource(id = R.string.email)) },
                        trailingIcon = {
                            ExposedDropdownMenuDefaults.TrailingIcon(
                                expanded = expandedDropdown
                            )
                        }
                    )

                    ExposedDropdownMenu(
                        modifier = Modifier.fillMaxWidth(),
                        expanded = expandedDropdown,
                        onDismissRequest = { expandedDropdown = false },
                    ) {
                        emails.forEach { e ->
                            DropdownMenuItem(
                                text = { Text(text = e) },
                                onClick = {
                                    email = e
                                    expandedDropdown = false
                                })
                        }
                    }
                }

                OutlinedTextField(modifier = Modifier.fillMaxWidth(),
                    value = password,
                    label = { Text(text = stringResource(id = R.string.password)) },
                    onValueChange = { password = it },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    visualTransformation = if (showPassword) {
                        VisualTransformation.None
                    } else {
                        PasswordVisualTransformation()
                    },
                    trailingIcon = {
                        if (showPassword) {
                            IconButton(onClick = { showPassword = false }) {
                                Icon(Icons.Default.VisibilityOff, contentDescription = null)
                            }
                        } else {
                            IconButton(onClick = { showPassword = true }) {
                                Icon(Icons.Filled.Visibility, contentDescription = null)
                            }
                        }
                    })

            }

            Button(modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 64.dp), onClick = {
                // Attempt authentication
                onAuthenticate.invoke(email, password)
            }

            ) {
                Text(text = stringResource(id = R.string.sign_in))
            }
        }
    }
}

@Preview
@Composable
fun AuthenticationOfflineScreenPreview() {
    LeishmaniappTheme {
        AuthenticationOfflineScreen(emails = List(10) { Specialist.mock().email })
        { _, _ -> }
    }
}