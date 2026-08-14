package com.example.swu_planner.data.repository

import android.util.Log
import com.example.swu_planner.data.api.SwuMobilityApi
import com.example.swu_planner.data.model.Departure

class DeparturesRepository(private val api: SwuMobilityApi) {

    suspend fun getDepartures(
        stopNumber: String,
        limit: Int = 10
    ): Result<List<Departure>> {
        return try {
            val response = api.getStopDepartures(stopNumber, limit)

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