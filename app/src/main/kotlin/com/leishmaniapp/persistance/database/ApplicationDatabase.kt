package com.leishmaniapp.persistance.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.leishmaniapp.entities.Patient
import com.leishmaniapp.entities.Specialist
import com.leishmaniapp.persistance.dao.DiagnosisDao
import com.leishmaniapp.persistance.dao.ImageDao
import com.leishmaniapp.persistance.dao.PatientDao
import com.leishmaniapp.persistance.dao.SpecialistDao
import com.leishmaniapp.persistance.entities.DiagnosisRoom
import com.leishmaniapp.persistance.entities.ImageRoom

@Database(
    entities = [Specialist::class, Patient::class, DiagnosisRoom::class, ImageRoom::class],
    version = 1
)
@TypeConverters(ApplicationRoomTypeConverters::class)
abstract class ApplicationDatabase : RoomDatabase() {
    abstract fun specialistDao(): SpecialistDao
    abstract fun patientDao(): PatientDao
    abstract fun diagnosisDao(): DiagnosisDao
    abstract fun imageDao(): ImageDao
}