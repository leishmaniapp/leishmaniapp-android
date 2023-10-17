package com.leishmaniapp.infrastructure.module

import com.leishmaniapp.infrastructure.cloud.CloudImageProcessing
import com.leishmaniapp.infrastructure.cloud.CloudProcessingRequest
import com.leishmaniapp.usecases.IImageProcessing
import com.leishmaniapp.usecases.IProcessingRequest
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class ProcessingRequestModule {
    @Binds
    @Singleton
    abstract fun provideProcessingRequest(processingRequest: CloudProcessingRequest): IProcessingRequest

    @Binds
    @Singleton
    abstract fun provideImageProcessing(imageProcessing: CloudImageProcessing): IImageProcessing
}