package com.example.swu_planner.data.dto

/**
 * Data Transfer Object for the response containing all active vehicle trips.
 */
data class VehicleTripResponse(
    val VehicleTrip: VehicleTripData
)

/**
 * Container for active vehicle trip list and metadata.
 */
data class VehicleTripData(
    val ServiceCategory: String,
    val UpdateInterval: String,
    val Filter: String,
    val ContentScope: String,
    val OrderedBy: String,
    val CurrentStatus: String,
    val CurrentTimestamp: String,
    val TripData: List<TripDto>
)

/**
 * DTO representing a single vehicle trip, including its current position and journey info.
 */
data class TripDto(
    val VehicleNumber: Int,
    val VehicleCategory: Int,
    val IsActive: Boolean,
    val PositionData: PositionDataDto? = null,
    val TimeData: TimeDataDto? = null,
    val JourneyData: JourneyDataDto? = null
)

/**
 * DTO for vehicle position data.
 */
data class PositionDataDto(
    val Longitude: Double,
    val Latitude: Double,
    val Bearing: Int? = null
)

/**
 * DTO for vehicle punctuality data.
 */
data class TimeDataDto(
    val Deviation: Int,
    val ReferenceTime: String
)

/**
 * DTO for journey information associated with a vehicle trip.
 */
data class JourneyDataDto(
    val RouteNumber: Int,
    val ArrivalDirectionText: String,
    val DepartureDirectionText: String,
    val Direction: Int
)
