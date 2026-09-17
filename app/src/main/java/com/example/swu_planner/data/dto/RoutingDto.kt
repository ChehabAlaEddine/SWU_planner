package com.example.swu_planner.data.dto

import com.google.gson.JsonElement
import com.google.gson.annotations.SerializedName

/**
 * Root Data Transfer Object for EFA journey search responses.
 */
data class EfaJourneysResponse(
    @SerializedName("trips") val trips: JsonElement? // Handle polymorphism at the 'trips' level
)

/**
 * DTO representing a single trip option between two locations.
 */
data class EfaTripDto(
    val duration: String?,
    val interchange: String?,
    val legs: List<EfaLegDto>?
)

/**
 * DTO representing a leg of a trip (a single vehicle ride or walk).
 */
data class EfaLegDto(
    val points: List<EfaPointDto>?,
    val mode: EfaModeDto?
)

/**
 * DTO representing a point in a trip leg (e.g., departure or arrival stop).
 */
data class EfaPointDto(
    val name: String?,
    val usage: String?, // departure, arrival
    val dateTime: EfaDateTimeDto?
)

/**
 * DTO representing date and time information, including real-time updates.
 */
data class EfaDateTimeDto(
    val date: String?,
    val time: String?,
    val rtDate: String?,
    val rtTime: String?
)

/**
 * DTO representing the mode of transport for a trip leg.
 */
data class EfaModeDto(
    val name: String?,
    val number: String?,
    val symbol: String?,
    val type: String?,
    val destination: String?
)
