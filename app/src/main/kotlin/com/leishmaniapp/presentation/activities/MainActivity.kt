package com.leishmaniapp.presentation.activities

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.OnBackPressedCallback
import androidx.activity.compose.setContent
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import com.leishmaniapp.presentation.navigation.RootNavigation
import com.leishmaniapp.presentation.ui.dialogs.WillPopScopeAlertDialog
import com.leishmaniapp.presentation.ui.theme.LeishmaniappTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Is user trying to exit the application?
        val willPopScope: MutableState<Boolean> = mutableStateOf(false)

        // Set the application content with RootNavigationGraph
        setContent {
            if (willPopScope.value) {
                WillPopScopeAlertDialog(
                    onDismissRequest = { willPopScope.value = false },
                    onConfirmExit = {
                        // Close the application
                        this.finishAffinity()
                    })
            }

            LeishmaniappTheme {
                RootNavigation()
            }
        }

        // Handle user trying to get out of the application
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                willPopScope.value = true
            }
        })
    }
}


