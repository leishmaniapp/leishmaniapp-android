package com.leishmaniapp.infrastructure.di

import com.leishmaniapp.domain.services.IAuthorizationService
import com.leishmaniapp.infrastructure.service.sharing.ApplicationDiagnosisSharingImpl
import com.leishmaniapp.infrastructure.service.picture.ApplicationPictureStandardizationImpl
import com.leishmaniapp.domain.services.IDiagnosisSharing
import com.leishmaniapp.domain.services.IOngoingDiagnosisService
import com.leishmaniapp.domain.services.IPictureStandardizationService
import com.leishmaniapp.domain.services.IQueuingService
import com.leishmaniapp.infrastructure.service.analysis.WorkQueuingServiceImpl
import com.leishmaniapp.infrastructure.service.preferences.DataStoreAuthorizationServiceImpl
import com.leishmaniapp.infrastructure.service.preferences.DataStoreOngoingDiagnosisServiceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

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
    @Singleton
    fun bindAuthorizationService(
        authorizationServiceImpl: DataStoreAuthorizationServiceImpl,
    ): IAuthorizationService

    /**
     * Provide the [IOngoingDiagnosisService] via [DataStoreOngoingDiagnosisServiceImpl]
     */
    @Binds
    @Singleton
    fun bindOngoingDiagnosisService(
        diagnosisServiceImpl: DataStoreOngoingDiagnosisServiceImpl,
    ): IOngoingDiagnosisService

    /**
     * Provide the [IQueuingService] via [WorkQueuingServiceImpl]
     */
    @Binds
    @Singleton
    fun bindQueuingService(
        queuingServiceImpl: WorkQueuingServiceImpl,
    ): IQueuingService

    /**
     * Provide the [IDiagnosisSharing] via [ApplicationDiagnosisSharingImpl]
     */
    @Binds
    @Singleton
    fun bindDiagnosisSharing(
        diagnosisSharing: ApplicationDiagnosisSharingImpl
    ): IDiagnosisSharing

    /**
     * Provide the [IPictureStandardizationService] via [ApplicationDiagnosisSharingImpl]
     */
    @Binds
    @Singleton
    fun bindPictureStandardization(
        pictureStandardizationImpl: ApplicationPictureStandardizationImpl
    ): IPictureStandardizationService
}