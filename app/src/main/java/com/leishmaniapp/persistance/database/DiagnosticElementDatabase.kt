package com.leishmaniapp.persistance.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.leishmaniapp.entities.DiagnosticElementRoom
import com.leishmaniapp.entities.SpecialistRoom
import com.leishmaniapp.persistance.dao.DiagnosticElementDao

@Database(
    entities = [DiagnosticElementRoom::class],
    version = 1
)
abstract class DiagnosticElementDatabase : RoomDatabase(){
    abstract fun diagnosticElementDao(): DiagnosticElementDao
}