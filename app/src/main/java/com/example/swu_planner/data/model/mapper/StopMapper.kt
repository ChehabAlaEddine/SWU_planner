package com.example.swu_planner.data.model.mapper

import com.example.swu_planner.data.model.Platform
import com.example.swu_planner.data.model.Route
import com.example.swu_planner.data.model.Stop
import com.example.swu_planner.data.model.StopDto
import com.example.swu_planner.data.model.StopPointDto

fun StopDto.toDomain() = Stop(
    number = StopNumber,
    name = StopName,
    latitude = StopCoordinates.Latitude,
    longitude = StopCoordinates.Longitude,
    platforms = StopPoints.map { it.toDomain() }
)

fun StopPointDto.toDomain() = Platform(
    code = StopPointCode,
    platformName = PlatformName,
    latitude = StopPointCoordinates.Latitude,
    longitude = StopPointCoordinates.Longitude,
    routes = ServicingRoutes.map { Route(it.RouteNumber, it.RouteName) }
)
