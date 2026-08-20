package com.example.swu_planner.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.swu_planner.data.local.entities.StopEntity

@Dao
interface StopDao {
    @Query("SELECT * FROM stops")
    suspend fun getAllStops(): List<StopEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(stops: List<StopEntity>)

    @Query("DELETE FROM stops")
    suspend fun clearAll()
}
