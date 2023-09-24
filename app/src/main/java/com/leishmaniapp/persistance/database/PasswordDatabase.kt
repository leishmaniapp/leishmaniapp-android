package com.leishmaniapp.persistance.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.leishmaniapp.entities.PasswordRoom
import com.leishmaniapp.entities.SpecialistRoom

@Database(
    entities = [PasswordRoom::class],
    version = 1
)
abstract class PasswordDatabase : RoomDatabase(){
    abstract fun passwordDao(): PasswordRoom
}