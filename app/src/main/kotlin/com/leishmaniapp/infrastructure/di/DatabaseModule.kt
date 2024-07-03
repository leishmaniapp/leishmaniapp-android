package com.leishmaniapp.infrastructure.di

import android.content.Context
import androidx.room.Room
import com.leishmaniapp.domain.repository.ICredentialsRepository
import com.leishmaniapp.domain.repository.IDiagnosesRepository
import com.leishmaniapp.domain.repository.ISamplesRepository
import com.leishmaniapp.domain.repository.IPatientsRepository
import com.leishmaniapp.domain.repository.ISpecialistsRepository
import com.leishmaniapp.infrastructure.persistance.ApplicationDatabase
import com.leishmaniapp.infrastructure.persistance.repository.RoomCredentialsRepositoryImpl
import com.leishmaniapp.infrastructure.persistance.repository.RoomDiagnosesRepositoryImpl
import com.leishmaniapp.infrastructure.persistance.repository.RoomPatientsRepositoryImpl
import com.leishmaniapp.infrastructure.persistance.repository.RoomSamplesRepositoryImpl
import com.leishmaniapp.infrastructure.persistance.repository.RoomSpecialistRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

/**
 * Provide dependency injection for the application database
 */
@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    /**
     * Provides [ApplicationDatabase] implementation
     */
    @Provides
    fun providerApplicationDatabase(@ApplicationContext context: Context): ApplicationDatabase =
        Room.databaseBuilder(context, ApplicationDatabase::class.java, "database").build()

    /**
     * Provide the [IDiagnosesRepository] using [RoomDiagnosesRepositoryImpl]
     */
    @Provides
    fun provideDiagnosesRepository(applicationDatabase: ApplicationDatabase): IDiagnosesRepository =
        RoomDiagnosesRepositoryImpl(applicationDatabase.diagnosesDao())

    /**
     * Provide the [ISamplesRepository] using [RoomSamplesRepositoryImpl]
     */
    @Provides
    fun provideImageSamplesRepository(applicationDatabase: ApplicationDatabase): ISamplesRepository =
        RoomSamplesRepositoryImpl(applicationDatabase.imageSamplesDao())

    /**
     * Provide the [IPatientsRepository] using [RoomPatientsRepositoryImpl]
     */
    @Provides
    fun providePatientsRepository(applicationDatabase: ApplicationDatabase): IPatientsRepository =
        RoomPatientsRepositoryImpl(applicationDatabase.patientsDao())

    /**
     * Provide the [ISpecialistsRepository] using [RoomSpecialistRepositoryImpl]
     */
    @Provides
    fun provideSpecialistsRepository(applicationDatabase: ApplicationDatabase): ISpecialistsRepository =
        RoomSpecialistRepositoryImpl(applicationDatabase.specialistsDao())

    @Provides
    fun provideCredentialsRepository(applicationDatabase: ApplicationDatabase): ICredentialsRepository =
        RoomCredentialsRepositoryImpl(applicationDatabase.credentialsDao())
}