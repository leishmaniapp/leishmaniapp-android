package com.leishmaniapp.presentation.activities

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.leishmaniapp.presentation.navigation.RootNavigation
import com.leishmaniapp.presentation.ui.theme.LeishmaniappTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LeishmaniappTheme {
                RootNavigation()
            }
        }
    }
}


