package com.leishmaniapp.infrastructure.di

import com.leishmaniapp.infrastructure.sharing.ApplicationDiagnosisSharing
import com.leishmaniapp.infrastructure.picture.ApplicationPictureStandardization
import com.leishmaniapp.domain.services.IDiagnosisSharing
import com.leishmaniapp.domain.services.IPictureStandardization
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Provides the use cases interfaces implementation
 */
@Module
@InstallIn(SingletonComponent::class)
abstract class ServicesModule {
    @Binds
    @Singleton
    abstract fun provideDiagnosisSharing(
        diagnosisSharing: ApplicationDiagnosisSharing
    ): IDiagnosisSharing

    @Binds
    @Singleton
    abstract fun providePictureStandardization(
        pictureStandardizationImpl: ApplicationPictureStandardization
    ): IPictureStandardization
}