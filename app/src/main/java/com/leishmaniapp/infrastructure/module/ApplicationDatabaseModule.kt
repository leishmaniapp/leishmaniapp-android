package com.leishmaniapp.infrastructure.module

import android.content.Context
import androidx.room.Room
import com.leishmaniapp.persistance.entities.DiagnosisRoom.Companion.asRoomEntity
import com.leishmaniapp.persistance.entities.ImageRoom.Companion.asRoomEntity
import com.leishmaniapp.utils.MockGenerator
import com.leishmaniapp.persistance.database.ApplicationDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.runBlocking
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ApplicationDatabaseModule {
    @Provides
    @Singleton
    fun providerApplicationDatabase(@ApplicationContext context: Context): ApplicationDatabase {
        val database =
            Room.databaseBuilder(context, ApplicationDatabase::class.java, "database").build()

        // TODO: Create database if no diagnosis are found
        runBlocking {
            if (database.diagnosisDao().allDiagnoses().isEmpty()) {

                List(10) {
                    MockGenerator.mockDiagnosis()
                }.forEach { diagnosis ->
                    database.specialistDao().upsertSpecialist(diagnosis.specialist)
                    database.patientDao().upsertPatient(diagnosis.patient)
                    database.diagnosisDao().upsertDiagnosis(diagnosis.asRoomEntity())

                    diagnosis.images.values.forEach { image ->
                        database.imageDao().upsertImage(image.asRoomEntity(diagnosis.id))
                    }
                }

            }
        }

        return database
    }
}