package com.leishmaniapp.presentation.activities


import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.leishmaniapp.entities.Diagnosis
import com.leishmaniapp.presentation.ui.MissingCameraPermission
import com.leishmaniapp.presentation.ui.theme.LeishmaniappTheme
import com.leishmaniapp.presentation.views.diagnosis.CameraScreen
import java.io.File
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class CameraActivity : ComponentActivity() {

    companion object {
        const val FILE_EXTENSION = ".jpg"
    }

    /* Permissions */

    /// Camera permission was granted
    private var cameraPermissionIsGranted = mutableStateOf(false)

    /// Current diagnosis
    private lateinit var diagnosis: Diagnosis

    /// File name
    private lateinit var outputDirectory: File

    /// Show a message asking for camera permission
    private var shouldShowCameraPermissionRationale = mutableStateOf(false)

    /// Launch camera permission dialog
    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        // Set camera permission
        cameraPermissionIsGranted.value = isGranted
        shouldShowCameraPermissionRationale.value = !isGranted

        Log.i(
            "PERMISSION", if (isGranted) "Permission granted" else "Permission Denied"
        )
    }

    /// Application settings intent result
    private var startSettingsForResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            requestCameraPermission()
        }

    /// Request for camera permissions and set the corresponding value
    private fun requestCameraPermission() {
        when {
            ContextCompat.checkSelfPermission(
                this, Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED -> {
                // Permission is granted
                cameraPermissionIsGranted.value = true
            }

            ActivityCompat.shouldShowRequestPermissionRationale(
                this, Manifest.permission.CAMERA
            ) -> {
                // Show rationale for camera request
                shouldShowCameraPermissionRationale.value = true
                requestPermissionLauncher.launch(Manifest.permission.CAMERA)
            }

            else -> {
                // Permission has not been asked yet
                shouldShowCameraPermissionRationale.value = false
                requestPermissionLauncher.launch(Manifest.permission.CAMERA)
            }
        }
    }

    /* Camera */

    /// Camera executor service thread
    private lateinit var cameraExecutor: ExecutorService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Get the current Diagnosis
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
            diagnosis = intent.extras!!.getParcelable("diagnosis", Diagnosis::class.java)!!
        } else {
            @Suppress("DEPRECATION")
            diagnosis = intent.extras!!.getParcelable("diagnosis")!!
        }

        // Create the directory if it doesn't exist
        outputDirectory = File(filesDir, String.format("diagnosis${File.separator}${diagnosis.id}"))
        if (!outputDirectory.exists()) {
            Log.d("DiagnosisFilesystem", outputDirectory.toString())
            outputDirectory.mkdirs()
        }

        // Initialize camera executor service thread
        cameraExecutor = Executors.newSingleThreadExecutor()

        // Show content
        setContent {
            LeishmaniappTheme {
                if (cameraPermissionIsGranted.value) {
                    // Show camera
                    CameraScreen(outputFile = File(
                        outputDirectory,
                        "${diagnosis.samples}${FILE_EXTENSION}"
                    ),
                        executor = cameraExecutor,
                        onImageCaptured = { uri ->
                            Log.d("DiagnosisCamera", "Photo taken with URI: $uri")
                            setResult(RESULT_OK, Intent().apply {
                                putExtra("uri", uri.toString())
                            })
                            finish()
                        },
                        onError = { Log.e("DiagnosisCamera", "Camera error: ", it) },
                        onCancel = {
                            // Finish activity without result
                            Log.d("DiagnosisCamera", "Canceled action")
                            setResult(RESULT_CANCELED)
                            finish()
                        })
                } else if (shouldShowCameraPermissionRationale.value) {
                    // Show rationale for camera request
                    MissingCameraPermission(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(32.dp)
                    ) {
                        // Start settings
                        startSettingsForResult.launch(
                            Intent(
                                Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.parse(
                                    "package:$packageName"
                                )
                            )
                        )
                    }
                } else {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }
            }
        }

        // Request permissions
        requestCameraPermission()
    }

    override fun onDestroy() {
        cameraExecutor.shutdown()
        super.onDestroy()
    }
}