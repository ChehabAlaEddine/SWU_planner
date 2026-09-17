package com.example.swu_planner.data.model

/**
 * Domain model representing a user-saved address.
 */
data class SavedAddress(
    val type: String,
    val name: String,
    val address: String,
    val latitude: Double,
    val longitude: Double,
    val iconName: String = "place"
)
