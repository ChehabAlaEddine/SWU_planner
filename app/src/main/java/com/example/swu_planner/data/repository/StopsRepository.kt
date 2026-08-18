package com.example.swu_planner.data.repository

import android.util.Log
import com.example.swu_planner.data.model.StopDto
import com.example.swu_planner.data.api.SwuMobilityApi


class StopsRepository(private val api: SwuMobilityApi) {
    suspend fun getAllStops(): Result<List<StopDto>> {
        return try {
            val response = api.getAllStops()
            if (response.StopAttributes.CurrentStatus?.lowercase() != "ok") {
                return Result.failure(
                    Exception("Failed to fetch stops (Status: ${response.StopAttributes.CurrentStatus})")
                )
            }
            val stops = response.StopAttributes.StopData
            stops.forEach { Log.d("Stop: ", it.toString()) }
            Result.success(stops)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getStop(stopNumber: String): Result<StopDto> {
        return try {
            val response = api.getStop(stopNumber)
            if (response.StopAttributes.CurrentStatus?.lowercase() != "ok") {
                return Result.failure(
                    Exception("Stop $stopNumber not found (Status: ${response.StopAttributes.CurrentStatus})")
                )
            }
            val stop = response.StopAttributes.StopData
            Log.d("Stop: ", stop.toString())
            Result.success(stop)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }


}