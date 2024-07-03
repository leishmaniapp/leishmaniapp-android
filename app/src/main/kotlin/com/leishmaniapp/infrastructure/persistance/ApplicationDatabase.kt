package com.leishmaniapp.infrastructure.persistance

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.leishmaniapp.infrastructure.persistance.dao.RoomCredentialsDao
import com.leishmaniapp.infrastructure.persistance.dao.RoomDiagnosesDao
import com.leishmaniapp.infrastructure.persistance.dao.RoomImagesDao
import com.leishmaniapp.infrastructure.persistance.dao.RoomPatientsDao
import com.leishmaniapp.infrastructure.persistance.dao.RoomSpecialistsDao
import com.leishmaniapp.infrastructure.persistance.entities.RoomCredentialsEntity
import com.leishmaniapp.infrastructure.persistance.entities.RoomDiagnosisEntity
import com.leishmaniapp.infrastructure.persistance.entities.RoomImageEntity
import com.leishmaniapp.infrastructure.persistance.entities.RoomPatientEntity
import com.leishmaniapp.infrastructure.persistance.entities.RoomSpecialistEntity

/**
 * Declare the Androidx Room database and all of its repositories
 */
@Database(
    entities = [
        RoomDiagnosisEntity::class,
        RoomImageEntity::class,
        RoomSpecialistEntity::class,
        RoomPatientEntity::class,
        RoomCredentialsEntity::class,
    ],
    version = 1
)
@TypeConverters(ApplicationRoomTypeConverters::class)
abstract class ApplicationDatabase : RoomDatabase() {
    abstract fun diagnosesDao(): RoomDiagnosesDao
    abstract fun imageSamplesDao(): RoomImagesDao
    abstract fun specialistsDao(): RoomSpecialistsDao
    abstract fun patientsDao(): RoomPatientsDao
    abstract fun credentialsDao(): RoomCredentialsDao
}
