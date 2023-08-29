package com.leishmaniapp.presentation.views


import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
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
import com.leishmaniapp.presentation.theme.LeishmaniappTheme
import com.leishmaniapp.presentation.theme.Purple41



@Composable
fun SignInActivity(){
    val emailState = remember { mutableStateOf(TextFieldValue("")) }
    val passwordState = remember { mutableStateOf(TextFieldValue("")) }

    LeishmaniappTheme {
        Surface(modifier = Modifier.fillMaxSize(), color = Color.Black) {
            Column(modifier = Modifier.fillMaxSize()) {
                Spacer(modifier = Modifier.height(118.dp))
                InLabel()
                Spacer(modifier = Modifier.height(123.dp))
                emailTextField(emailState.value, onEmailValueChange = { emailState.value = it })
                Spacer(modifier = Modifier.height(46.dp))
                passwordTextField(passwordState.value, onPasswordValueChange = { passwordState.value = it })
                Spacer(modifier = Modifier.height(234.dp))
                continueB(start = R.string.button_comenzar)
            }
        }
    }
}

@Composable
fun InLabel(){
    Text(
        text = stringResource(id = R.string.label_sign_in),
        style = TextStyle(
            fontSize = 30.sp,
            fontWeight = FontWeight(700),
            color = Color(0xFFFFFFFF),
            //textAlign = TextAlign.Center
        ),
        modifier = Modifier
            .width(280.dp)
            .height(39.dp)
            .padding(horizontal = 40.dp)
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun emailTextField(emailValue: TextFieldValue, onEmailValueChange: (TextFieldValue) -> Unit){
    OutlinedTextField(
        value = emailValue,
        label = { Text(text = "Nombre de usuario") },
        onValueChange = onEmailValueChange,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp),
        colors = TextFieldDefaults.outlinedTextFieldColors(
            focusedBorderColor = Purple41,
            unfocusedBorderColor = Color.White,
            focusedLabelColor = Color.White,
            unfocusedLabelColor = Color.White,
            focusedTextColor =  Color.White,
            unfocusedTextColor = Color.White
        )
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun passwordTextField(passwordValue: TextFieldValue, onPasswordValueChange: (TextFieldValue) -> Unit){
    OutlinedTextField(
        value = passwordValue,
        label = { Text(text = "Contraseña") },
        onValueChange = onPasswordValueChange,
        textStyle = TextStyle(color = Color.White),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp),
        colors = TextFieldDefaults.outlinedTextFieldColors(
            focusedBorderColor = Purple41,
            unfocusedBorderColor = Color.White,
            focusedLabelColor = Color.White,
            unfocusedLabelColor = Color.White,
            focusedTextColor =  Color.White,
            unfocusedTextColor = Color.White
        ),
        visualTransformation = PasswordVisualTransformation(),
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Password,
        )
    )
}

@Composable
fun continueB (start : Int){
    val context = LocalContext.current
    Button(
        onClick = {
            //val intent = Intent(context, DiseasesMenuActivity::class.java)
            //context.startActivity(intent)
        },
        colors = ButtonDefaults.buttonColors(containerColor = Purple41),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 86.dp)
    ){
        Text(
            text = stringResource(id = start),
            style = TextStyle(
                fontSize = 20.sp,
                //fontFamily = FontFamily(Font(R.font.inter)),
                fontWeight = FontWeight(500),
                color = Color(0xFFFFFFFF),
                textAlign = TextAlign.Center,
            )
        )
    }
}

@Preview
@Composable
fun signInPreview(){
    LeishmaniappTheme {
        Surface(modifier = Modifier.fillMaxSize(), color = Color.Black) {
            SignInActivity()
        }
    }
}