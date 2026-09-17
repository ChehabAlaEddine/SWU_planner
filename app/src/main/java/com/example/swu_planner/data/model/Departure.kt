package com.example.swu_planner.data.model

/**
 * Domain model representing a vehicle departure from a stop.
 *
 * @property StopPointNumber Unique identifier for the platform/stop point.
 * @property StopPointCode Detailed code for the stop point (e.g., "de:08421:1000:0:D2").
 * @property PlatformName Human-readable platform name (e.g., "D", "E").
 * @property VehicleNumber Unique identifier for the vehicle, if available.
 * @property VehicleCategory Category of the vehicle (e.g., "Bus", "Tram").
 * @property RouteNumber The numeric identifier of the route.
 * @property RouteName The display name of the route.
 * @property Status The punctuality status (e.g., 1 for on-time).
 * @property DepartureDirectionText The destination or direction of the trip.
 * @property DepartureTimeScheduled The scheduled time of departure.
 * @property DepartureTimeActual The expected/actual time of departure.
 * @property DepartureCountdown Seconds remaining until departure.
 * @property DepartureDeviation Deviation from the schedule in seconds (positive for late).
 */
data class Departure(
    val StopPointNumber: Int,
    val StopPointCode: String,
    val PlatformName: String?,
    val VehicleNumber: Int?,
    val VehicleCategory: String?,
    val RouteNumber: Int,
    val RouteName: String,
    val Status: Int,
    val DepartureDirectionText: String,
    val DepartureTimeScheduled: String,
    val DepartureTimeActual: String,
    val DepartureCountdown: Int,
    val DepartureDeviation: Int
)
