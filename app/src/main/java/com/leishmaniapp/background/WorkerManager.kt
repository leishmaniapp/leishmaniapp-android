package com.leishmaniapp.background

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.work.BackoffPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.leishmaniapp.presentation.ui.theme.LeishmaniappTheme
import dagger.hilt.android.AndroidEntryPoint
import java.time.Duration

import com.leishmaniapp.background.CustomWorker

@AndroidEntryPoint
class WorkerManager : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val workRequest = OneTimeWorkRequestBuilder<CustomWorker>()
            .setInitialDelay(Duration.ofSeconds(5))
            .setBackoffCriteria(
                backoffPolicy = BackoffPolicy.LINEAR,
                duration = Duration.ofSeconds(15)
            )
            .build()

        WorkManager.getInstance(applicationContext).enqueue(workRequest)


        setContent {
            LeishmaniappTheme {

            }
        }
    }
}