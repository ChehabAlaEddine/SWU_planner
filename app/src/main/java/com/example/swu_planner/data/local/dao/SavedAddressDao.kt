package com.example.swu_planner.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.swu_planner.data.local.entities.SavedAddressEntity
import kotlinx.coroutines.flow.Flow

/**
 * Data Access Object for the `saved_addresses` table.
 */
@Dao
interface SavedAddressDao {
    /**
     * Returns a [Flow] of all saved addresses.
     */
    @Query("SELECT * FROM saved_addresses")
    fun getAllSavedAddressesFlow(): Flow<List<SavedAddressEntity>>

    /**
     * Retrieves a saved address by its type (e.g., "home", "work").
     */
    @Query("SELECT * FROM saved_addresses WHERE type = :type LIMIT 1")
    suspend fun getAddressByType(type: String): SavedAddressEntity?

    /**
     * Inserts or replaces a saved address.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAddress(address: SavedAddressEntity)

    /**
     * Deletes a saved address by its type.
     */
    @Query("DELETE FROM saved_addresses WHERE type = :type")
    suspend fun deleteAddressByType(type: String)
}
