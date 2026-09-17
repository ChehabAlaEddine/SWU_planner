package com.example.swu_planner.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Database entity representing a user's saved address (e.g., Home or Work).
 *
 * @property type The unique type identifier (e.g., "home", "work").
 * @property name A descriptive name for the address.
 * @property address The formatted address string.
 * @property latitude The geographical latitude.
 * @property longitude The geographical longitude.
 */
@Entity(tableName = "saved_addresses")
data class SavedAddressEntity(
    @PrimaryKey
    val type: String, // "home", "work", etc.
    val name: String,
    val address: String,
    val latitude: Double,
    val longitude: Double,
    val iconName: String = "place"
)
