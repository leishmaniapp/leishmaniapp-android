package com.leishmaniapp.infrastructure.di

import com.leishmaniapp.infrastructure.sharing.ApplicationDiagnosisSharingImpl
import com.leishmaniapp.infrastructure.picture.ApplicationPictureStandardizationImpl
import com.leishmaniapp.domain.services.IDiagnosisSharing
import com.leishmaniapp.domain.services.INetworkService
import com.leishmaniapp.domain.services.IPictureStandardization
import com.leishmaniapp.infrastructure.android.network.ConnectivityManagerNetworkServiceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

/**
 * Provides the services implementations via DI for local application services
 */
@Module
@InstallIn(SingletonComponent::class)
interface ApplicationServicesModule {
    /**
     * Provide the [IDiagnosisSharing] via [ApplicationDiagnosisSharingImpl]
     */
    @Binds
    fun provideDiagnosisSharing(
        diagnosisSharing: ApplicationDiagnosisSharingImpl
    ): IDiagnosisSharing

    /**
     * Provide the [IPictureStandardization] via [ApplicationDiagnosisSharingImpl]
     */
    @Binds
    fun providePictureStandardization(
        pictureStandardizationImpl: ApplicationPictureStandardizationImpl
    ): IPictureStandardization

    /**
     * Provide the [INetworkService] via [ConnectivityManagerNetworkServiceImpl]
     */
    @Binds
    fun provideNetworkService(
        networkService: ConnectivityManagerNetworkServiceImpl
    ): INetworkService
}