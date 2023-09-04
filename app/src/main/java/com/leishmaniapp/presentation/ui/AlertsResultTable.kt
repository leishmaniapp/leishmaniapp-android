package com.leishmaniapp.presentation.ui

import android.util.Log
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.tooling.preview.Preview
import com.leishmaniapp.presentation.ui.theme.LeishmaniappTheme


@Composable
fun AlertResultTable(
    show: Boolean,
    titleText: String? = null,
    description: String,
    confirmButtonText: String,
    onConfirm: () -> Int,
    cancelButtonText: String,
    onCancel: () -> Unit,
    onDismiss: () -> Unit,

    ){
    val title : @Composable (() -> Unit)? = if (titleText.isNullOrBlank())
        null
    else {
        { Text(text = titleText)}
    }



    AlertDialog(
        title = title,
        text = {
            Text(text = description)
        },
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick =
            {
                onConfirm()
                onDismiss()
            })
            {
                Text(text = confirmButtonText)
            }
        },
        dismissButton = {
            TextButton(onClick = {
                onCancel()
                onDismiss()
            }) {
               Text(text = cancelButtonText)
            }
        }

    )
}



@Composable
@Preview
fun AlertResultTablePreview(){
    var showAlert by rememberSaveable { mutableStateOf(true)}
    LeishmaniappTheme {
        AlertResultTable(
            show = true,
            titleText = "Alerta",
            description = "No puede continuar el diagnóstico" ,
            confirmButtonText = "Aceptar" ,
            onConfirm = { Log.d("confrima","confirmando alerta") },
            cancelButtonText = "Cancelar",
            onCancel = { showAlert = false },
        ) { showAlert = false }
    }
    }