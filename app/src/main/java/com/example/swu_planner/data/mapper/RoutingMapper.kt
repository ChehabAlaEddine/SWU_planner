package com.example.swu_planner.data.mapper

import com.example.swu_planner.data.dto.JourneyDto
import com.example.swu_planner.data.dto.LegDto
import com.example.swu_planner.data.local.entities.SavedAddressEntity
import com.example.swu_planner.data.model.Journey
import com.example.swu_planner.data.model.Leg
import com.example.swu_planner.data.model.SavedAddress
import java.time.Duration
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

fun JourneyDto.toDomain(): Journey {
    val legsDomain = legs?.map { it.toDomain() } ?: emptyList()
    val firstDeparture = legsDomain.firstOrNull()?.departureTime ?: ""
    val lastArrival = legsDomain.lastOrNull()?.arrivalTime ?: ""
    
    val durationText = try {
        val start = ZonedDateTime.parse(legs?.firstOrNull()?.departure)
        val end = ZonedDateTime.parse(legs?.lastOrNull()?.arrival)
        val diff = Duration.between(start, end)
        "${diff.toMinutes()} min"
    } catch (e: Exception) {
        ""
    }

    return Journey(
        departureTime = firstDeparture,
        arrivalTime = lastArrival,
        duration = durationText,
        legs = legsDomain
    )
}

fun LegDto.toDomain() = Leg(
    origin = origin?.name ?: "",
    destination = destination?.name ?: "",
    lineName = line?.name,
    lineMode = line?.mode,
    departureTime = try { departure?.substring(11, 16) ?: "" } catch (e: Exception) { "" },
    arrivalTime = try { arrival?.substring(11, 16) ?: "" } catch (e: Exception) { "" }
)

fun SavedAddressEntity.toDomain() = SavedAddress(
    type = type,
    name = name,
    address = address,
    latitude = latitude,
    longitude = longitude
)

fun SavedAddress.toEntity() = SavedAddressEntity(
    type = type,
    name = name,
    address = address,
    latitude = latitude,
    longitude = longitude
)
