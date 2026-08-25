package com.example.swu_planner.data.repository

import android.util.Log
import com.example.swu_planner.data.api.SwuMobilityApi
import com.example.swu_planner.data.dto.StopBaseDataResponse
import com.example.swu_planner.data.dto.StopDto
import com.example.swu_planner.data.local.dao.StopDao
import com.example.swu_planner.data.local.entities.toDto
import com.example.swu_planner.data.local.entities.toEntity
import javax.inject.Inject


class StopsRepository @Inject constructor(
    private val api: SwuMobilityApi,
    private val stopDao: StopDao
) {
    suspend fun getAllStops(): Result<List<StopDto>> {
        return try {
            // 1. Try to get from local cache
            val cachedStops = stopDao.getAllStops()
            if (cachedStops.isNotEmpty()) {
                Log.d("StopsRepository", "Loading stops from cache: ${cachedStops.size}")
                return Result.success(cachedStops.map { it.toDto() })
            }

            // 2. Fetch from API if cache is empty
            Log.d("StopsRepository", "Cache empty, fetching from API")
            val response: StopBaseDataResponse<List<StopDto>> = api.getAllStops()
            if (response.StopAttributes.CurrentStatus.lowercase() != "ok") {
                return Result.failure(
                    Exception("Failed to fetch stops (Status: ${response.StopAttributes.CurrentStatus})")
                )
            }
            val stops = response.StopAttributes.StopData
            
            // 3. Save to cache
            stopDao.insertAll(stops.map { it.toEntity() })
            
            Result.success(stops)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getStop(stopNumber: String): Result<StopDto> {
        return try {
            val response: StopBaseDataResponse<StopDto> = api.getStop(stopNumber)
            if (response.StopAttributes.CurrentStatus.lowercase() != "ok") {
                return Result.failure(
                    Exception("Stop $stopNumber not found (Status: ${response.StopAttributes.CurrentStatus})")
                )
            }
            val stop = response.StopAttributes.StopData
            Result.success(stop)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
