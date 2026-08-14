package com.example.swu_planner.data.model

data class Stop(
    val number: Int,
    val name: String,
    val latitude: Double,
    val longitude: Double,
    val platforms: List<Platform>
)

data class Platform(
    val code: String,
    val platformName: String,
    val latitude: Double,
    val longitude: Double,
    val routes: List<Route>
)

data class Route(
    val number: Int,
    val name: String
)