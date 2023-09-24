package com.leishmaniapp.persistance.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.leishmaniapp.entities.UsernameRoom

@Dao
interface UsernameDao {
    @Upsert
    suspend fun upsertUsername(username: UsernameRoom)

    @Delete
    suspend fun deleteUsername(username: UsernameRoom)

    @Query("SELECT * FROM usernameroom")
    suspend fun getPassword() : List<UsernameRoom>
}