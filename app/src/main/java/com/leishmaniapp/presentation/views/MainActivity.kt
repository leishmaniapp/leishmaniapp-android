package com.leishmaniapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.leishmaniapp.presentation.theme.LeishmaniappTheme
import com.leishmaniapp.presentation.ui.*


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LeishmaniappTheme {
               previewPatientList()
            }

        }
                }
    }





