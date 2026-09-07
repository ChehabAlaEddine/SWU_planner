package com.example.swu_planner.data.model

data class Journey(
    val departureTime: String,
    val arrivalTime: String,
    val duration: String,
    val legs: List<Leg>
)

data class Leg(
    val origin: String,
    val destination: String,
    val lineName: String?,
    val lineMode: String?,
    val departureTime: String,
    val arrivalTime: String
)

data class SavedAddress(
    val type: String,
    val name: String,
    val address: String,
    val latitude: Double,
    val longitude: Double
)
