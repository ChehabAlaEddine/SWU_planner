package com.example.swu_planner.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.swu_planner.data.local.entities.StopEntity

/**
 * Data Access Object for the `stops` table.
 */
@Dao
interface StopDao {
    /**
     * Returns all cached stops.
     */
    @Query("SELECT * FROM stops")
    suspend fun getAllStops(): List<StopEntity>

    /**
     * Inserts a list of stops, replacing any duplicates.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(stops: List<StopEntity>)

    /**
     * Deletes all cached stops.
     */
    @Query("DELETE FROM stops")
    suspend fun clearAll()
}
