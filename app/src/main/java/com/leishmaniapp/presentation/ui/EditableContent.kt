package com.leishmaniapp.presentation.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import com.leishmaniapp.presentation.ui.theme.LeishmaniappTheme

@Composable
fun <T> EditableContent(
    modifier: Modifier = Modifier,
    content: T,
    stringToContent: (String) -> T,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    textStyle: TextStyle = TextStyle.Default,
    emptyValue: String = "-",
    onEdit: (T) -> Unit
) {
    BasicTextField(
        modifier = modifier,
        keyboardOptions = keyboardOptions,
        textStyle = textStyle,
        value = content.toString().ifEmpty { emptyValue },
        onValueChange = { newValue ->
            try {
                onEdit.invoke(stringToContent(newValue))
            } catch (_: Exception) {
            }
        },
    )
}

@Composable
@Preview(showBackground = true)
fun EditableContentPreview() {
    var content by remember {
        mutableStateOf("Hello")
    }
    LeishmaniappTheme {
        EditableContent(content = content, stringToContent = { it }) { value ->
            content = value
        }
    }
}

@Composable
@Preview(showBackground = true)
fun EditableContentPreview_Empty() {
    var content by remember {
        mutableStateOf("")
    }

    LeishmaniappTheme {
        EditableContent(content = content, stringToContent = { it }) { value ->
            content = value
        }
    }
}