package com.example.swu_planner.data.dto

import com.google.gson.annotations.SerializedName

data class JourneysResponse(
    val journeys: List<JourneyDto>?
)

data class JourneyDto(
    val legs: List<LegDto>?,
    val refreshToken: String?
)

data class LegDto(
    val origin: LocationDto?,
    val destination: LocationDto?,
    val departure: String?, // ISO 8601
    val arrival: String?,   // ISO 8601
    val line: LineDto?,
    val direction: String?
)

data class LocationDto(
    val id: String?,
    val name: String?,
    val location: CoordinatesDto?
)

data class LineDto(
    val id: String?,
    val name: String?,
    val mode: String?, // bus, tram, etc.
    val product: String?
)
