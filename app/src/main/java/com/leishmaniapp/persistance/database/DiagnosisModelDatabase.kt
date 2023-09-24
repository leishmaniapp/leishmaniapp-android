package com.leishmaniapp.persistance.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.leishmaniapp.entities.DiagnosisModelRoom
import com.leishmaniapp.persistance.dao.DiagnosisModelDao


@Database(
    entities = [DiagnosisModelRoom::class],
    version = 1
)
abstract class DiagnosisModelDatabase: RoomDatabase() {
    abstract  fun diagnosisModelDao(): DiagnosisModelDao
}
