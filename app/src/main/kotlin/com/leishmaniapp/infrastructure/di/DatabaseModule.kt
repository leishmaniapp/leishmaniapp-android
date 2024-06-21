package com.leishmaniapp.infrastructure.di

import android.content.Context
import androidx.room.Room
import com.leishmaniapp.domain.repository.DiagnosesRepository
import com.leishmaniapp.domain.repository.ImageSamplesRepository
import com.leishmaniapp.domain.repository.PatientsRepository
import com.leishmaniapp.domain.repository.SpecialistsRepository
import com.leishmaniapp.infrastructure.persistance.ApplicationDatabase
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
     * Provide the [DiagnosesRepository] using the [ApplicationDatabase]
     */
    @Provides
    fun provideDiagnosesRepository(applicationDatabase: ApplicationDatabase): DiagnosesRepository =
        applicationDatabase.diagnosesRepository()

    /**
     * Provide the [ImageSamplesRepository] using the [ApplicationDatabase]
     */
    @Provides
    fun provideImageSamplesRepository(applicationDatabase: ApplicationDatabase): ImageSamplesRepository =
        applicationDatabase.imageSamplesRepository()

    /**
     * Provide the [PatientsRepository] using the [ApplicationDatabase]
     */
    @Provides
    fun providePatientsRepository(applicationDatabase: ApplicationDatabase): PatientsRepository =
        applicationDatabase.patientsRepository()

    /**
     * Provide the [SpecialistsRepository] using the [ApplicationDatabase]
     */
    @Provides
    fun provideSpecialistsRepository(applicationDatabase: ApplicationDatabase): SpecialistsRepository =
        applicationDatabase.specialistsRepository()
}