package com.example.swu_planner.data.model

/**
 * Domain model representing a public transport stop.
 */
data class Stop(
    val number: Int,
    val name: String,
    val latitude: Double,
    val longitude: Double,
    val platforms: List<Platform>
)

/**
 * Domain model representing a platform or stop point within a stop.
 */
data class Platform(
    val code: String,
    val platformName: String,
    val latitude: Double,
    val longitude: Double,
    val routes: List<Route>
)
