package com.example.swu_planner.data.repository

import android.util.Log
import com.example.swu_planner.data.api.SwuMobilityApi
import com.example.swu_planner.data.dto.DeparturesResponse
import com.example.swu_planner.data.model.Departure
import javax.inject.Inject

/**
 * Repository for fetching vehicle departures from a specific stop.
 */
class DeparturesRepository @Inject constructor(private val api: SwuMobilityApi) {

    /**
     * Fetches departures for the given stop number.
     *
     * @param stopNumber The identifier for the stop.
     * @param limit The maximum number of departures to fetch.
     * @return A [Result] containing a list of [Departure]s.
     */
    suspend fun getDepartures(
        stopNumber: String,
        limit: Int = 10
    ): Result<List<Departure>> {
        return try {
            val response: DeparturesResponse = api.getStopDepartures(stopNumber, limit)

            // Check if stop was found
            if (response.StopPassage.State.lowercase() != "ok") {
                return Result.failure(
                    Exception("Stop $stopNumber not found (State: ${response.StopPassage.State})")
                )
            }

            // Extract departures
            val departures = response.StopPassage.DepartureData ?: emptyList()
            departures.forEach { Log.d("Departure: ", it.toString()) }
            Result.success(departures)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
