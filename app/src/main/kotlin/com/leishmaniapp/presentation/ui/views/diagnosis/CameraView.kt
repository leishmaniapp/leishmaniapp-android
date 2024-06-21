package com.leishmaniapp.presentation.ui.views.diagnosis

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import com.leishmaniapp.domain.entities.Diagnosis
import com.leishmaniapp.presentation.activities.CameraActivity


@Composable
fun CameraView(diagnosis: com.leishmaniapp.domain.entities.Diagnosis, onPictureTake: (Uri) -> Unit, onCanceled: () -> Unit) {
    val context = LocalContext.current
    val startCameraActivity =
        rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) { activityResult ->
            if (activityResult.resultCode == RESULT_OK) {
                // Invoke callback with URI
                onPictureTake.invoke(
                    Uri.parse(activityResult.data!!.extras!!.getString("uri")!!)
                )
            } else {
                onCanceled.invoke()
            }
        }

    LaunchedEffect(Unit) {
        startCameraActivity.launch(Intent(context, CameraActivity::class.java).apply {
            putExtra("diagnosis", diagnosis)
        })
    }
}