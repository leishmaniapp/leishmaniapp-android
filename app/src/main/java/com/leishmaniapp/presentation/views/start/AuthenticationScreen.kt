package com.leishmaniapp.presentation.views.start


import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.leishmaniapp.R
import com.leishmaniapp.entities.Password
import com.leishmaniapp.entities.Username
import com.leishmaniapp.presentation.ui.theme.LeishmaniappTheme

/**
 * @view A02
 */
@Composable
fun AuthenticationScreen(
    authenticationInProgress: Boolean = false,
    onAuthenticate: (Username, Password) -> Unit,
) {

    var emailState by remember { mutableStateOf(TextFieldValue("")) }
    var passwordState by remember { mutableStateOf(TextFieldValue("")) }

    LeishmaniappTheme {
        Surface(modifier = Modifier.fillMaxSize(), color = Color.Black) {
            Column(
                modifier = Modifier.fillMaxSize(),
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
                        style = TextStyle(
                            fontSize = 30.sp,
                            fontWeight = FontWeight(700),
                            color = Color.White,
                        ),
                    )
                }

                MaterialTheme(darkColorScheme()) {
                    Column(modifier = Modifier.weight(1f)) {
                        OutlinedTextField(
                            value = emailState,
                            label = { Text(text = stringResource(id = R.string.email)) },
                            onValueChange = { emailState = it },
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Email,
                            )
                        )

                        OutlinedTextField(
                            value = passwordState,
                            label = { Text(text = stringResource(id = R.string.password)) },
                            onValueChange = { passwordState = it },
                            visualTransformation = PasswordVisualTransformation(),
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Password,
                            )
                        )
                    }
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
                        onClick = if (!authenticationInProgress) {
                            {
                                // Attempt authentication
                                onAuthenticate.invoke(
                                    Username(emailState.text), Password(passwordState.text)
                                )
                            }
                        } else {
                            { /* Authentication is in progress, do nothing */ }
                        }
                    ) {
                        if (!authenticationInProgress) {
                            Text(text = stringResource(id = R.string.sign_in))
                        } else {
                            CircularProgressIndicator(
                                color = MaterialTheme.colorScheme.onPrimary,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun AuthenticationScreenPreview_NotInProgress() {
    LeishmaniappTheme {
        AuthenticationScreen(authenticationInProgress = false) { _, _ -> }
    }
}

@Preview
@Composable
fun AuthenticationScreenPreview_InProgress() {
    LeishmaniappTheme {
        AuthenticationScreen(authenticationInProgress = true) { _, _ -> }
    }
}