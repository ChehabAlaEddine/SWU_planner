package com.example.swu_planner.data.model

data class VehicleTripResponse(
    val VehicleTrip: VehicleTripData
)

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

data class TripDto(
    val VehicleNumber: Int,
    val VehicleCategory: Int,
    val IsActive: Boolean,
    val PositionData: PositionDataDto? = null,
    val TimeData: TimeDataDto? = null,
    val JourneyData: JourneyDataDto? = null
)

data class PositionDataDto(
    val Longitude: Double,
    val Latitude: Double,
    val Bearing: Int? = null
)

data class TimeDataDto(
    val Deviation: Int,
    val ReferenceTime: String
)

data class JourneyDataDto(
    val RouteNumber: Int,
    val ArrivalDirectionText: String,
    val DepartureDirectionText: String,
    val Direction: Int
)
