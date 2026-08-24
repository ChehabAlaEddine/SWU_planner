package com.example.swu_planner.data.repository

import android.util.Log
import com.example.swu_planner.data.api.SwuMobilityApi
import com.example.swu_planner.data.model.Trip
import com.example.swu_planner.data.model.VehiclePassage
import com.example.swu_planner.data.model.mapper.toDomain
import javax.inject.Inject

class VehicleRepository @Inject constructor(private val api: SwuMobilityApi) {
    suspend fun getActiveTrips(): Result<List<Trip>> {
        return try {
            val response = api.getAllTrips()
            if (response.VehicleTrip.CurrentStatus?.lowercase() != "ok") {
                return Result.failure(
                    Exception("Failed to fetch vehicle trips (Status: ${response.VehicleTrip.CurrentStatus})")
                )
            }
            val activeTrips = response.VehicleTrip.TripData
                .filter { it.IsActive }
                .map { it.toDomain() }
            Result.success(activeTrips)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getVehiclePassage(vehicleNumber: String): Result<List<VehiclePassage>> {
        return try {
            val response = api.getVehiclePassage(vehicleNumber)
            if (response.VehiclePassage.State?.lowercase() != "ok") {
                return Result.failure(
                    Exception("Failed to fetch vehicle passage (Status: ${response.VehiclePassage.State})")
                )
            }
            val passages = response.VehiclePassage.PassageData?.map { it.toDomain() } ?: emptyList()
            Log.d("VehicleRepository", "Fetched passages: $passages")
            Result.success(passages)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
