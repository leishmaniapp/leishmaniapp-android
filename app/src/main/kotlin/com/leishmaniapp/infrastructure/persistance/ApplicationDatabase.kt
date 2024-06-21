package com.leishmaniapp.infrastructure.persistance

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.leishmaniapp.domain.entities.Diagnosis
import com.leishmaniapp.domain.entities.ImageSample
import com.leishmaniapp.domain.entities.Patient
import com.leishmaniapp.domain.entities.Specialist
import com.leishmaniapp.domain.repository.DiagnosesRepository
import com.leishmaniapp.domain.repository.ImageSamplesRepository
import com.leishmaniapp.domain.repository.PatientsRepository
import com.leishmaniapp.domain.repository.SpecialistsRepository

/**
 * Declare the Androidx Room database and all of its repositories
 */
@Database(
    entities = [
        Diagnosis::class,
        ImageSample::class,
        Specialist::class,
        Patient::class,
    ],
    version = 2
)
@TypeConverters(ApplicationRoomTypeConverters::class)
abstract class ApplicationDatabase : RoomDatabase() {
    abstract fun diagnosesRepository(): DiagnosesRepository
    abstract fun imageSamplesRepository(): ImageSamplesRepository
    abstract fun specialistsRepository(): SpecialistsRepository
    abstract fun patientsRepository(): PatientsRepository
}
