package com.example.swu_planner.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.swu_planner.data.local.dao.SavedAddressDao
import com.example.swu_planner.data.local.dao.StopDao
import com.example.swu_planner.data.local.entities.SavedAddressEntity
import com.example.swu_planner.data.local.entities.StopEntity

/**
 * The main Room database for the application.
 *
 * Persists stops data and user-saved addresses.
 */
@Database(entities = [StopEntity::class, SavedAddressEntity::class], version = 2, exportSchema = false)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    /**
     * Provides access to the [StopDao] for interacting with stop data.
     */
    abstract fun stopDao(): StopDao

    /**
     * Provides access to the [SavedAddressDao] for interacting with user-saved addresses.
     */
    abstract fun savedAddressDao(): SavedAddressDao
}
