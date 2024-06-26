package com.leishmaniapp.presentation.ui.views.camera

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.Settings
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.leishmaniapp.presentation.ui.dialogs.MissingCameraPermissionAlertDialog

/**
 * Handle camera permissions before showing [content]
 */
@Composable
fun CameraPermissionHandler(content: @Composable () -> Unit) {

    // Get the current composable context
    val context = LocalContext.current

    // Get the initial permission status
    var isPermissionGranted by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(
                context, Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED
        )
    }

    // Request permissions
    val permissionRequestHandler =
        rememberLauncherForActivityResult(
            contract = ActivityResultContracts.RequestPermission(),
        ) { isPermissionGranted = it }

    // Request permission just once
    LaunchedEffect(key1 = true) {
        if (!isPermissionGranted)
            permissionRequestHandler.launch(Manifest.permission.CAMERA)
    }

    if (isPermissionGranted) {
        // Show the content
        content()
    } else {
        // Show the camera permission dialog
        Surface(modifier = Modifier.fillMaxSize()) {
            MissingCameraPermissionAlertDialog(
                onCheckAgain = { permissionRequestHandler.launch(Manifest.permission.CAMERA) },
                onRequestPermission = {
                    // Show request permission
                    if (ActivityCompat.shouldShowRequestPermissionRationale(
                            context as Activity,
                            Manifest.permission.CAMERA
                        )
                    ) permissionRequestHandler.launch(Manifest.permission.CAMERA)
                    // Denied, open settings
                    else {
                        context.startActivity(Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                            setData(Uri.fromParts("package", context.packageName, null))
                        })
                    }
                })
        }
    }
}