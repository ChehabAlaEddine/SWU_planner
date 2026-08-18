package com.example.swu_planner.data.model.mapper

import com.example.swu_planner.data.model.Trip
import com.example.swu_planner.data.model.TripDto

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
