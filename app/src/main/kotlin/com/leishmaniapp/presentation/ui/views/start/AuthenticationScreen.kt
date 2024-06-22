package com.leishmaniapp.presentation.ui.views.start

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
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
import com.leishmaniapp.domain.types.Email
import com.leishmaniapp.domain.types.Password
import com.leishmaniapp.presentation.ui.layout.LeishmaniappScaffold
import com.leishmaniapp.presentation.ui.theme.LeishmaniappTheme

/**
 * Show login screen
 * @view A02
 */
@Composable
fun AuthenticationScreen(
    onAuthenticate: (Email, Password) -> Unit,
) {

    var email by rememberSaveable { mutableStateOf("") }
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
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(32.dp)
                    .weight(1f),
                contentAlignment = Alignment.BottomStart
            ) {
                Text(
                    text = stringResource(id = R.string.sign_in),
                    textAlign = TextAlign.Start,
                    style = MaterialTheme.typography.headlineMedium,
                )
            }

            Column(modifier = Modifier.weight(1f)) {
                OutlinedTextField(
                    value = email,
                    label = { Text(text = stringResource(id = R.string.username)) },
                    onValueChange = { email = it },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Email,
                    )
                )

                OutlinedTextField(
                    value = password,
                    label = { Text(text = stringResource(id = R.string.password)) },
                    onValueChange = { password = it },
                    visualTransformation = if (showPassword) {
                        VisualTransformation.None
                    } else {
                        PasswordVisualTransformation()
                    },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Password,
                    ),
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
                    }
                )
            }

            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
            ) {
                Button(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 64.dp),
                    onClick = {
                        // Attempt authentication
                        onAuthenticate.invoke(email, password)
                    }

                ) {
                    Text(text = stringResource(id = R.string.sign_in))
                }
            }
        }
    }
}

@Preview
@Composable
fun AuthenticationScreenPreview() {
    LeishmaniappTheme {
        AuthenticationScreen { _, _ -> }
    }
}