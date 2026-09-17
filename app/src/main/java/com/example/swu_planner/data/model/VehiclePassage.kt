package com.example.swu_planner.data.model

/**
 * Domain model representing a stop in a specific vehicle's current trip.
 */
data class VehiclePassage(
    val stopNumber: Int,
    val stopName: String,
    val platformName: String?,
    val arrivalTimeScheduled: String?,
    val arrivalTimeActual: String?,
    val departureTimeScheduled: String?,
    val departureTimeActual: String?,
    val countdown: Int?,
    val deviation: Int?
)
