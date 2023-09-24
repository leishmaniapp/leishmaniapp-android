package com.leishmaniapp.persistance.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.leishmaniapp.entities.SpecialistDiagnosticElementRoom
import com.leishmaniapp.persistance.dao.SpecialistDiagnosticElementDao

@Database(
    entities = [SpecialistDiagnosticElementRoom::class],
    version = 1
)
abstract class SpecialistDiagnosticElementDatabase : RoomDatabase() {
    abstract fun specialistDiagnosticElementDao() : SpecialistDiagnosticElementDao
}