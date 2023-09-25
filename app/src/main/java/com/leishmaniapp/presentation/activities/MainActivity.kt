package com.leishmaniapp.presentation.activities

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.OnBackPressedCallback
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import com.leishmaniapp.presentation.navigation.RootNavigation
import com.leishmaniapp.presentation.ui.WillPopScopeAlertDialog
import com.leishmaniapp.presentation.ui.theme.LeishmaniappTheme
import com.leishmaniapp.presentation.viewmodel.ApplicationViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    // Create application global ViewModel
    private val applicationViewModel: ApplicationViewModel by viewModels()

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
                RootNavigation(applicationViewModel = applicationViewModel)
            }
        }

        // Handle user trying to get out of the application
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                Log.d(this::class.simpleName, "WillPopScope: User is trying to exit")
                willPopScope.value = true;
            }
        })
    }

}


