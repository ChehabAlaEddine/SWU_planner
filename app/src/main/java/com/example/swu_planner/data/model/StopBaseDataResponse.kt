package com.example.swu_planner.data.model


data class StopBaseDataResponse<T>(
    val StopAttributes: StopAttributes<T>
)

data class StopAttributes<T>(
    val CurrentStatus: String,
    val StopData: T
)

data class StopDto(
    val StopNumber: Int,
    val StopCode: String,
    val StopName: String,
    val StopAnnouncement: String?,
    val StopCoordinates: CoordinatesDto,
    val StopPoints: List<StopPointDto>
)

data class CoordinatesDto(
    val Longitude: Double,
    val Latitude: Double,
    val Bearing: Int? = null
)

data class StopPointDto(
    val StopPointCode: String,
    val PlatformName: String,
    val StopPointName: String,
    val StopPointCoordinates: CoordinatesDto,
    val ServicingRoutes: List<ServicingRouteDto>
)

data class ServicingRouteDto(
    val RouteNumber: Int,
    val RouteName: String
)