package com.leishmaniapp.persistance.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.leishmaniapp.entities.DiagnosticElementName
import com.leishmaniapp.persistance.dao.DiagnosisElementNameDao

@Database(
    entities = [DiagnosticElementName::class],
    version = 1,
    )
abstract class DiagnosisElementNameDatabase : RoomDatabase() {
    abstract fun diagnosisElementNameDao(): DiagnosisElementNameDao
}