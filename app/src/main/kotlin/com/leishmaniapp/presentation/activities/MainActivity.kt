package com.leishmaniapp.presentation.activities

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.OnBackPressedCallback
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.leishmaniapp.infrastructure.camera.CameraCalibrationAnalyzer
import com.leishmaniapp.presentation.navigation.RootNavigation
import com.leishmaniapp.presentation.ui.dialogs.WillPopScopeAlertDialog
import com.leishmaniapp.presentation.ui.theme.LeishmaniappTheme
import com.leishmaniapp.presentation.ui.views.camera.CameraPermissionHandler
import com.leishmaniapp.presentation.ui.views.camera.CameraScreen
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.coroutineScope

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

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU
            && ContextCompat.checkSelfPermission(
                applicationContext,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this, arrayOf(
                    Manifest.permission.POST_NOTIFICATIONS
                ), 0
            )
        }
    }
}


