package com.example.swu_planner.data.model

/**
 * Domain model representing a complete journey between two points.
 */
data class Journey(
    val departureTime: String,
    val arrivalTime: String,
    val duration: String,
    val legs: List<Leg>
)

/**
 * Domain model representing a single leg of a journey.
 */
data class Leg(
    val origin: String,
    val destination: String,
    val lineName: String?,
    val lineMode: String?,
    val departureTime: String,
    val arrivalTime: String
)
