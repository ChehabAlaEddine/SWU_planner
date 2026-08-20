package com.example.swu_planner.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.swu_planner.data.local.dao.StopDao
import com.example.swu_planner.data.local.entities.StopEntity

@Database(entities = [StopEntity::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun stopDao(): StopDao
}
