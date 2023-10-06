package com.leishmaniapp.infrastructure.module

import com.leishmaniapp.infrastructure.PDFDiagnosisSharing
import com.leishmaniapp.usecases.types.IDiagnosisSharing
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DiagnosisSharingModule {
    @Provides
    @Singleton
    fun provideDiagnosisSharing(): IDiagnosisSharing = PDFDiagnosisSharing()
}