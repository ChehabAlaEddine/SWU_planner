package com.example.swu_planner.data.mapper

import com.example.swu_planner.data.dto.StopDto
import com.example.swu_planner.data.dto.StopPointDto
import com.example.swu_planner.data.model.Platform
import com.example.swu_planner.data.model.Route
import com.example.swu_planner.data.model.Stop

/**
 * Extension function to map a [StopDto] to a domain [Stop] model.
 */
fun StopDto.toDomain() = Stop(
    number = StopNumber,
    name = StopName,
    latitude = StopCoordinates.Latitude,
    longitude = StopCoordinates.Longitude,
    platforms = StopPoints.map { it.toDomain() }
)

/**
 * Extension function to map a [StopPointDto] to a domain [Platform] model.
 */
fun StopPointDto.toDomain() = Platform(
    code = StopPointCode,
    platformName = PlatformName,
    latitude = StopPointCoordinates.Latitude,
    longitude = StopPointCoordinates.Longitude,
    routes = ServicingRoutes.map { Route(it.RouteNumber, it.RouteName) }
)
