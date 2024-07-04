package com.leishmaniapp.infrastructure.di

import com.leishmaniapp.domain.services.IAuthorizationService
import com.leishmaniapp.infrastructure.sharing.ApplicationDiagnosisSharingImpl
import com.leishmaniapp.infrastructure.picture.ApplicationPictureStandardizationImpl
import com.leishmaniapp.domain.services.IDiagnosisSharing
import com.leishmaniapp.domain.services.INetworkService
import com.leishmaniapp.domain.services.IPictureStandardization
import com.leishmaniapp.domain.services.IQueuingService
import com.leishmaniapp.infrastructure.analysis.WorkQueuingServiceImpl
import com.leishmaniapp.infrastructure.android.network.ConnectivityManagerNetworkServiceImpl
import com.leishmaniapp.infrastructure.preferences.DataStoreAuthorizationServiceImpl
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
     * Provide the [IAuthorizationService] via [DataStoreAuthorizationServiceImpl]
     */
    @Binds
    fun bindTokenRepository(
        tokenRepositoryImpl: DataStoreAuthorizationServiceImpl,
    ): IAuthorizationService

    /**
     * Provide the [IQueuingService] via [WorkQueuingServiceImpl]
     */
    @Binds
    fun bindQueuingService(
        queuingServiceImpl: WorkQueuingServiceImpl,
    ): IQueuingService

    /**
     * Provide the [IDiagnosisSharing] via [ApplicationDiagnosisSharingImpl]
     */
    @Binds
    fun dingDiagnosisSharing(
        diagnosisSharing: ApplicationDiagnosisSharingImpl
    ): IDiagnosisSharing

    /**
     * Provide the [IPictureStandardization] via [ApplicationDiagnosisSharingImpl]
     */
    @Binds
    fun bindPictureStandardization(
        pictureStandardizationImpl: ApplicationPictureStandardizationImpl
    ): IPictureStandardization

    /**
     * Provide the [INetworkService] via [ConnectivityManagerNetworkServiceImpl]
     */
    @Binds
    fun bindNetworkService(
        networkService: ConnectivityManagerNetworkServiceImpl
    ): INetworkService
}