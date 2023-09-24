package com.leishmaniapp.persistance.dao

import androidx.room.Query
import androidx.room.Upsert
import com.leishmaniapp.entities.PasswordRoom

interface PasswordDao {
    @Upsert
    suspend fun upsertPassword(password: PasswordRoom)
    @Query("SELECT * FROM passwordroom WHERE value = :password")
    suspend fun getPassword(password: PasswordRoom): PasswordRoom
}