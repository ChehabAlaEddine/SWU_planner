package com.example.swu_planner.data.mapper

import com.example.swu_planner.data.dto.TripDto
import com.example.swu_planner.data.model.Trip

/**
 * Extension function to map a [TripDto] to a domain [Trip] model.
 */
fun TripDto.toDomain() = Trip(
    vehicleNumber = VehicleNumber,
    vehicleCategory = VehicleCategory,
    isActive = IsActive,
    latitude = PositionData?.Latitude,
    longitude = PositionData?.Longitude,
    bearing = PositionData?.Bearing,
    routeNumber = JourneyData?.RouteNumber,
    destination = JourneyData?.ArrivalDirectionText,
    deviation = TimeData?.Deviation,
    direction = JourneyData?.Direction
)
