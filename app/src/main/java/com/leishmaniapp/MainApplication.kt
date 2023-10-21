package com.leishmaniapp

import android.app.Application
import android.util.Log
import androidx.work.Configuration
import com.leishmaniapp.infrastructure.background.ImageProcessingWorkerFactory
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class MainApplication : Application(), Configuration.Provider {
    @Inject
    lateinit var workerFactory: ImageProcessingWorkerFactory
    override fun getWorkManagerConfiguration(): Configuration =
        Configuration.Builder()
            .setMinimumLoggingLevel(Log.DEBUG)
            .setWorkerFactory(workerFactory)
            .build()
}