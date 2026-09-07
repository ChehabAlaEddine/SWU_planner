package com.example.swu_planner.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.swu_planner.data.local.entities.SavedAddressEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface SavedAddressDao {
    @Query("SELECT * FROM saved_addresses")
    fun getAllSavedAddressesFlow(): Flow<List<SavedAddressEntity>>

    @Query("SELECT * FROM saved_addresses WHERE type = :type LIMIT 1")
    suspend fun getAddressByType(type: String): SavedAddressEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAddress(address: SavedAddressEntity)

    @Query("DELETE FROM saved_addresses WHERE type = :type")
    suspend fun deleteAddressByType(type: String)
}
