package com.leishmaniapp

import android.app.Application
import android.util.Log
import androidx.work.Configuration
import com.amplifyframework.AmplifyException
import com.amplifyframework.auth.cognito.AWSCognitoAuthPlugin
import com.amplifyframework.core.AmplifyConfiguration
import com.amplifyframework.kotlin.core.Amplify
import com.amplifyframework.storage.s3.AWSS3StoragePlugin
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

    override fun onCreate() {
        super.onCreate()

        try {
            with(Amplify) {
                // Add plugins
                addPlugin(AWSCognitoAuthPlugin())
                addPlugin(AWSS3StoragePlugin())
                // Configure with debug option
                configure(
                    AmplifyConfiguration.builder(this@MainApplication.applicationContext)
                        .devMenuEnabled(BuildConfig.DEBUG)
                        .build(),
                    this@MainApplication.applicationContext
                )
            }
        } catch (error: AmplifyException) {
            Log.e("AWSAmplify", "Failed to initialize Amplify", error)
        }
    }
}