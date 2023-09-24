package com.leishmaniapp.persistance.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.leishmaniapp.entities.disease.Disease
import com.leishmaniapp.persistance.dao.DiseaseDao

@Database(
    entities = [Disease::class],
    version = 1
)

abstract class DiseaseDataBase: RoomDatabase() {
    abstract fun diseaseDao(): DiseaseDao
}