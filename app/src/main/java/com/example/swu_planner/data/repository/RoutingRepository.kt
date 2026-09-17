package com.example.swu_planner.data.repository

import android.util.Log
import com.example.swu_planner.data.api.RoutingApi
import com.example.swu_planner.data.local.dao.SavedAddressDao
import com.example.swu_planner.data.local.dao.StopDao
import com.example.swu_planner.data.local.entities.toDto
import com.example.swu_planner.data.mapper.toDomain
import com.example.swu_planner.data.mapper.toEntity
import com.example.swu_planner.data.model.Journey
import com.example.swu_planner.data.model.SavedAddress
import com.example.swu_planner.data.model.Stop
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

/**
 * Repository for journey planning, stop search, and saved addresses.
 */
class RoutingRepository @Inject constructor(
    private val routingApi: RoutingApi,
    private val stopDao: StopDao,
    private val savedAddressDao: SavedAddressDao
) {
    companion object {
        private const val TAG = "RoutingRepository"
        private const val TYPE_COORD = "coord"
        private const val TYPE_STOP = "stop"
        private const val COORD_FORMAT = "WGS84[dd.ddddd]"
    }

    /**
     * Finds potential journeys between two locations.
     *
     * @param from Origin location (name or "lat,lon").
     * @param to Destination location (name or "lat,lon").
     * @return A [Result] containing a list of [Journey]s.
     */
    suspend fun findJourneys(from: String, to: String): Result<List<Journey>> {
        val fromLocation = formatLocation(from)
        val toLocation = formatLocation(to)
        
        Log.d(TAG, "findJourneys: from=$from (${fromLocation.value}, ${fromLocation.type}), to=$to (${toLocation.value}, ${toLocation.type})")

        return try {
            val response = routingApi.getJourneys(
                origin = fromLocation.value,
                originType = fromLocation.type,
                destination = toLocation.value,
                destinationType = toLocation.type
            )
            val journeys = response.toDomain()
            Log.d(TAG, "findJourneys: found ${journeys.size} journeys")
            Result.success(journeys)
        } catch (e: Exception) {
            Log.e(TAG, "findJourneys: failed with error: ${e.message}", e)
            Result.failure(e)
        }
    }

    /**
     * Formats a location input string into a format suitable for the Routing API.
     *
     * If the input is a comma-separated coordinate pair (lat, lon), it formats it
     * as an EFA coordinate string ("Long:Lat:WGS84\[dd.ddddd\]") and returns type "coord".
     * Otherwise, it treats it as a stop name and returns type "stop".
     *
     * @param input The raw location string (e.g., "52.52,13.40" or "Berlin Hbf").
     * @return A [LocationInfo] containing the formatted value and its type.
     */
    private fun formatLocation(input: String): LocationInfo {
        val parts = input.split(",")
        if (parts.size == 2) {
            val lat = parts[0].trim()
            val lon = parts[1].trim()
            
            if (lat.toDoubleOrNull() != null && lon.toDoubleOrNull() != null) {
                // EFA expects coordinate order: Longitude:Latitude
                return LocationInfo(
                    value = "$lon:$lat:$COORD_FORMAT",
                    type = TYPE_COORD
                )
            }
        }
        return LocationInfo(value = input, type = TYPE_STOP)
    }

    private data class LocationInfo(val value: String, val type: String)

    /**
     * Provides stop suggestions for a search query.
     */
    suspend fun getStopHints(query: String): List<Stop> {
        val allStops = stopDao.getAllStops()
        return allStops
            .filter { it.stopName.contains(query, ignoreCase = true) }
            .take(5)
            .map { it.toDto().toDomain() }
    }

    /**
     * Returns a [Flow] of user-saved addresses.
     */
    fun getSavedAddressesFlow(): Flow<List<SavedAddress>> {
        return savedAddressDao.getAllSavedAddressesFlow().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    /**
     * Saves an address to the local database.
     */
    suspend fun saveAddress(address: SavedAddress) {
        savedAddressDao.insertAddress(address.toEntity())
    }

    /**
     * Retrieves a saved address by its type.
     */
    suspend fun getAddressByType(type: String): SavedAddress? {
        return savedAddressDao.getAddressByType(type)?.toDomain()
    }

    /**
     * Deletes a saved address by its type.
     */
    suspend fun deleteAddress(type: String) {
        savedAddressDao.deleteAddressByType(type)
    }
}
