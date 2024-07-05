package com.leishmaniapp.infrastructure.android

import android.app.Application
import android.util.Log
import androidx.camera.camera2.Camera2Config
import androidx.camera.core.CameraSelector
import androidx.camera.core.CameraXConfig
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.Configuration
import dagger.hilt.android.HiltAndroidApp
import java.util.concurrent.Executors
import javax.inject.Inject

@HiltAndroidApp
class MainApplication : Application(), CameraXConfig.Provider, Configuration.Provider {

    /**
     * Get the Jetpack Hilt factory for Jetpack WorkManager
     */
    @Inject
    lateinit var workerFactory: HiltWorkerFactory

    /**
     * Generate the CameraX configuration
     */
    override fun getCameraXConfig(): CameraXConfig =
        CameraXConfig.Builder.fromConfig(Camera2Config.defaultConfig())
            .setAvailableCamerasLimiter(CameraSelector.DEFAULT_BACK_CAMERA)
            .setCameraExecutor(Executors.newSingleThreadExecutor()).setMinimumLoggingLevel(Log.INFO)
            .build()

    /**
     * Generate the WorkManager configuration
     */
    override fun getWorkManagerConfiguration(): Configuration = Configuration.Builder()
        .setWorkerFactory(workerFactory)
        .build()
}