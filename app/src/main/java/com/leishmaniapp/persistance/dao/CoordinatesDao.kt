package com.leishmaniapp.persistance.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.leishmaniapp.entities.CoordinatesRoom

@Dao
interface CoordinatesDao {
    @Upsert
    suspend fun upsertCoordinates(coordinates: CoordinatesRoom)

    @Delete
    suspend fun deleteCoordinates(coordinates: CoordinatesRoom)

    @Query("Select * FROM coordinatesroom")
    suspend fun getAllCoordinates(): List<CoordinatesRoom>
}