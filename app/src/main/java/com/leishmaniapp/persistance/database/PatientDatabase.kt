package com.leishmaniapp.persistance.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.leishmaniapp.entities.PatientRoom
import com.leishmaniapp.persistance.dao.PatientDao

@Database(
    entities = [PatientRoom::class],
    version = 1
)
abstract class PatientDatabase : RoomDatabase(){
    abstract fun patientDao(): PatientDao
}