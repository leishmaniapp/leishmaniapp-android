package com.leishmaniapp.presentation.ui

import android.content.res.loader.ResourcesProvider
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.leishmaniapp.R
import com.leishmaniapp.presentation.theme.LeishmaniappTheme
import androidx.compose.material3.Text as Text

@Composable
fun AppNameHeader(){
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(85.dp).background(Color.Black)
    ){
        Text(text = stringResource(R.string.app_name),
            fontWeight = FontWeight.ExtraBold,
            color = Color.White,
            fontSize = 35.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(80.dp, 20.dp, 0.dp,5.dp))
    }
}

@Preview
@Composable
fun AppNameHeaderPreview(){
    LeishmaniappTheme {
        AppNameHeader()
    }
}