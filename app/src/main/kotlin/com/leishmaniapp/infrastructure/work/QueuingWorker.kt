package com.leishmaniapp.infrastructure.work

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.leishmaniapp.domain.repository.ISamplesRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

@HiltWorker
class QueuingWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted workerParameters: WorkerParameters,

    private val samplesRepository: ISamplesRepository,

    ) : CoroutineWorker(context, workerParameters) {

    override suspend fun doWork(): Result {
        TODO("Not yet implemented")
    }
}