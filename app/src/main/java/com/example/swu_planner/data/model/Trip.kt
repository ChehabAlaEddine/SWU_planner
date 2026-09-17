package com.example.swu_planner.data.model

/**
 * Domain model representing an active vehicle trip and its current status.
 */
data class Trip(
    val vehicleNumber: Int,
    val vehicleCategory: Int,
    val isActive: Boolean,
    val latitude: Double?,
    val longitude: Double?,
    val bearing: Int?,
    val routeNumber: Int?,
    val destination: String?,
    val deviation: Int?,
    val direction: Int?
)
