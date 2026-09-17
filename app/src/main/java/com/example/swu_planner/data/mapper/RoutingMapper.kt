package com.example.swu_planner.data.mapper

import android.util.Log
import com.example.swu_planner.data.dto.EfaJourneysResponse
import com.example.swu_planner.data.dto.EfaLegDto
import com.example.swu_planner.data.dto.EfaTripDto
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.example.swu_planner.data.local.entities.SavedAddressEntity
import com.example.swu_planner.data.model.Journey
import com.example.swu_planner.data.model.Leg
import com.example.swu_planner.data.model.SavedAddress

/**
 * Extension function to map an [EfaJourneysResponse] to a domain [Journey] model list.
 */
fun EfaJourneysResponse.toDomain(): List<Journey> {
    val tripsElement = trips ?: return emptyList()
    val gson = Gson()
    val tag = "RoutingMapper"

    // EFA's "trips" field can be:
    // 1. A JSON Array: {"trips": [...]}
    // 2. A JSON Object containing "trip": {"trips": {"trip": [...] or {"trip": {...}}}}

    val tripList: List<EfaTripDto> = try {
        when {
            tripsElement.isJsonArray -> {
                Log.d(tag, "trips is a JsonArray")
                val type = object : TypeToken<List<EfaTripDto>>() {}.type
                gson.fromJson(tripsElement, type)
            }
            tripsElement.isJsonObject -> {
                val tripsObj = tripsElement.asJsonObject
                val trip = tripsObj.get("trip")

                when {
                    trip == null -> {
                        Log.w(tag, "trips is an object but no 'trip' field found")
                        emptyList()
                    }
                    trip.isJsonArray -> {
                        Log.d(tag, "trips.trip is a JsonArray")
                        val type = object : TypeToken<List<EfaTripDto>>() {}.type
                        gson.fromJson(trip, type)
                    }
                    trip.isJsonObject -> {
                        Log.d(tag, "trips.trip is a JsonObject")
                        listOf(gson.fromJson(trip, EfaTripDto::class.java))
                    }
                    else -> {
                        Log.w(tag, "trips.trip is unexpected type: ${trip.javaClass.simpleName}")
                        emptyList()
                    }
                }
            }
            else -> {
                Log.w(tag, "trips is unexpected type: ${tripsElement.javaClass.simpleName}")
                emptyList()
            }
        }
    } catch (e: Exception) {
        Log.e(tag, "Error parsing trips JSON: ${e.message}", e)
        emptyList()
    }

    return tripList.map { it.toDomain() }
}

/**
 * Extension function to map an [EfaTripDto] to a domain [Journey] model.
 */
fun EfaTripDto.toDomain(): Journey {
    val legsDomain = legs?.map { it.toDomain() } ?: emptyList()
    val firstDeparture = legsDomain.firstOrNull()?.departureTime ?: ""
    val lastArrival = legsDomain.lastOrNull()?.arrivalTime ?: ""
    
    val durationText = duration?.let { 
        val parts = it.split(":")
        if (parts.size == 2) {
            val mins = parts[0].toInt() * 60 + parts[1].toInt()
            "$mins min"
        } else it
    } ?: ""

    return Journey(
        departureTime = firstDeparture,
        arrivalTime = lastArrival,
        duration = durationText,
        legs = legsDomain
    )
}

/**
 * Extension function to map an [EfaLegDto] to a domain [Leg] model.
 */
fun EfaLegDto.toDomain(): Leg {
    val departurePoint = points?.find { it.usage == "departure" }
    val arrivalPoint = points?.find { it.usage == "arrival" }
    
    val rawLineName = mode?.symbol ?: mode?.number
    val lineName = if (rawLineName.isNullOrBlank()) null else rawLineName
    
    return Leg(
        origin = departurePoint?.name ?: "",
        destination = arrivalPoint?.name ?: "",
        lineName = lineName,
        lineMode = mode?.name,
        departureTime = departurePoint?.dateTime?.rtTime ?: departurePoint?.dateTime?.time ?: "",
        arrivalTime = arrivalPoint?.dateTime?.rtTime ?: arrivalPoint?.dateTime?.time ?: ""
    )
}

/**
 * Extension function to map a [SavedAddressEntity] to a domain [SavedAddress] model.
 */
fun SavedAddressEntity.toDomain() = SavedAddress(
    type = type,
    name = name,
    address = address,
    latitude = latitude,
    longitude = longitude,
    iconName = iconName
)

/**
 * Extension function to map a domain [SavedAddress] to a [SavedAddressEntity].
 */
fun SavedAddress.toEntity() = SavedAddressEntity(
    type = type,
    name = name,
    address = address,
    latitude = latitude,
    longitude = longitude,
    iconName = iconName
)
