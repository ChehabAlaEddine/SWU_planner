package com.example.swu_planner.data.model


data class Departure(
    val StopPointNumber: Int,              // Platform/stop point ID
    val StopPointCode: String,             // "de:08421:1000:0:D2"
    val PlatformName: String?,             // "D" or "E"
    val VehicleNumber: Int?,               // Nullable
    val VehicleCategory: String?,          // Nullable
    val RouteNumber: Int,                  // 11 or 12
    val RouteName: String,                 // "11" or "12"
    val Status: Int,                       // 1 = on time, etc
    val DepartureDirectionText: String,    // "Ringverkehr", "Unterweiler"
    val DepartureTimeScheduled: String,    // ISO 8601 timestamp
    val DepartureTimeActual: String,       // ISO 8601 timestamp
    val DepartureCountdown: Int,           // Seconds until departure
    val DepartureDeviation: Int            // Deviation in seconds (0 = on time)
)