package com.example.swu_planner.data.dto

/**
 * Data Transfer Object for the vehicle passage response, showing stops a vehicle will visit.
 */
data class VehiclePassageResponse(
    val VehiclePassage: VehiclePassageData
)

/**
 * Container for detailed vehicle passage data.
 */
data class VehiclePassageData(
    val ServiceCategory: String,
    val UpdateInterval: String,
    val ContentScope: String,
    val Range: String,
    val VehicleNumber: Int,
    val VehicleCategory: Int,
    val RouteNumber: Int,
    val RouteName: String,
    val OrderedBy: String,
    val State: String,
    val CurrentTimestamp: String,
    val PassageData: List<PassageDto>?
)

/**
 * DTO representing a stop in a vehicle's passage, including schedule and real-time data.
 */
data class PassageDto(
    val StopNumber: Int,
    val StopCode: String,
    val StopName: String,
    val StopPointNumber: Int,
    val StopPointCode: String,
    val PlatformName: String?,
    val StopPointName: String?,
    val ArrivalDirectionText: String?,
    val DepartureDirectionText: String?,
    val DepartureTimeScheduled: String?,
    val DepartureTimeActual: String?,
    val DepartureCountdown: Int?,
    val DepartureDeviation: Int?,
    val ArrivalTimeScheduled: String?,
    val ArrivalTimeActual: String?,
    val ArrivalCountdown: Int?,
    val ArrivalDeviation: Int?,
    val Status: Int?,
    val SequenceNumber: Int?
)
