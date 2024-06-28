package com.leishmaniapp.presentation.ui.layout

import android.content.res.Configuration
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.ImageProxy
import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.leishmaniapp.R
import com.leishmaniapp.domain.calibration.ImageCalibrationData
import com.leishmaniapp.presentation.ui.composables.CameraCalibrationCard
import com.leishmaniapp.presentation.ui.theme.LeishmaniappTheme
import com.leishmaniapp.utilities.mock.MockGenerator.mock

@Composable
fun CameraLayout(
    onCancel: () -> Unit,
    onPhotoTake: () -> Unit,
    calibration: @Composable () -> Unit,
    preview: @Composable ColumnScope.() -> Unit,
) {
    Surface(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Box(modifier = Modifier.fillMaxWidth()) {

                // Invoke the calibration data
                calibration()

                IconButton(
                    modifier = Modifier
                        .padding(8.dp)
                        .align(Alignment.TopEnd),
                    onClick = onCancel
                ) {
                    Icon(
                        modifier = Modifier.size(24.dp),
                        imageVector = Icons.Filled.Cancel,
                        contentDescription = stringResource(id = R.string.cancel_camera),
                    )
                }
            }

            // Invoke the preview
            preview()

            Button(
                modifier = Modifier.padding(8.dp),
                onClick = onPhotoTake,
            ) {
                Icon(
                    imageVector = Icons.Filled.PhotoCamera,
                    contentDescription = stringResource(id = R.string.take_photo),
                )
            }
        }
    }
}

@Composable
@Preview
private fun CameraLayoutPreview() {
    LeishmaniappTheme {
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            CameraLayout(
                onCancel = { },
                onPhotoTake = { },
                calibration = {
                    CameraCalibrationCard(
                        modifier = Modifier.padding(8.dp),
                        calibrationData = ImageCalibrationData.mock()
                    )
                }) {

                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                        .background(color = Color.Black),
                ) {
                    Image(
                        modifier = Modifier.fillMaxSize(),
                        painter = painterResource(id = R.drawable.macrophage),
                        contentDescription = null
                    )
                }
            }
        }
    }
}

@Composable
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
private fun CameraLayoutPreview_DarkTheme() {
    LeishmaniappTheme {
        LeishmaniappScaffold { paddingValues ->
            Box(
                modifier = Modifier
                    .padding(paddingValues)
                    .fillMaxSize()
            ) {
                CameraLayout(
                    onCancel = { },
                    onPhotoTake = { },
                    calibration = {
                        CameraCalibrationCard(
                            modifier = Modifier.padding(8.dp),
                            calibrationData = ImageCalibrationData.mock()
                        )
                    }) {

                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth()
                            .background(color = Color.Black),
                    ) {
                        Image(
                            modifier = Modifier.fillMaxSize(),
                            painter = painterResource(id = R.drawable.macrophage),
                            contentDescription = null
                        )
                    }
                }
            }
        }
    }
}