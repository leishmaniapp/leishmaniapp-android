package com.leishmaniapp.persistance.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.leishmaniapp.entities.Diagnosis
import com.leishmaniapp.entities.DiagnosisRoom
import com.leishmaniapp.persistance.dao.DiagnosisDao

@Database(
    entities = [DiagnosisRoom::class],
    version = 1
)
abstract class DiagnosisDatabase: RoomDatabase() {
    abstract  fun diagnosisDao(): DiagnosisDao
}