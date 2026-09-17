package com.example.swu_planner.data.dto

/**
 * Wrapper DTO for stop attribute responses.
 */
data class StopBaseDataResponse<T>(
    val StopAttributes: StopAttributes<T>
)

/**
 * Metadata and stop data container.
 */
data class StopAttributes<T>(
    val CurrentStatus: String,
    val StopData: T
)

/**
 * Data Transfer Object representing a public transport stop.
 */
data class StopDto(
    val StopNumber: Int,
    val StopCode: String,
    val StopName: String,
    val StopAnnouncement: String?,
    val StopCoordinates: CoordinatesDto,
    val StopPoints: List<StopPointDto>
)

/**
 * Data Transfer Object for geographical coordinates.
 */
data class CoordinatesDto(
    val Longitude: Double,
    val Latitude: Double,
    val Bearing: Int? = null
)

/**
 * Data Transfer Object representing a specific point/platform at a stop.
 */
data class StopPointDto(
    val StopPointCode: String,
    val PlatformName: String,
    val StopPointName: String,
    val StopPointCoordinates: CoordinatesDto,
    val ServicingRoutes: List<ServicingRouteDto>
)

/**
 * Data Transfer Object for a route servicing a stop point.
 */
data class ServicingRouteDto(
    val RouteNumber: Int,
    val RouteName: String
)
