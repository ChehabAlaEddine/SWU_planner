package com.example.swu_planner.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "saved_addresses")
data class SavedAddressEntity(
    @PrimaryKey
    val type: String, // "home", "work", etc.
    val name: String,
    val address: String,
    val latitude: Double,
    val longitude: Double
)
