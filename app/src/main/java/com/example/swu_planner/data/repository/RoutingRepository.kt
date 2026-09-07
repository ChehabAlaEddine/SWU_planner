package com.example.swu_planner.data.repository

import com.example.swu_planner.data.api.RoutingApi
import com.example.swu_planner.data.local.dao.SavedAddressDao
import com.example.swu_planner.data.local.dao.StopDao
import com.example.swu_planner.data.local.entities.SavedAddressEntity
import com.example.swu_planner.data.mapper.toDomain
import com.example.swu_planner.data.mapper.toEntity
import com.example.swu_planner.data.model.Journey
import com.example.swu_planner.data.model.SavedAddress
import com.example.swu_planner.data.model.Stop
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class RoutingRepository @Inject constructor(
    private val routingApi: RoutingApi,
    private val stopDao: StopDao,
    private val savedAddressDao: SavedAddressDao
) {
    suspend fun findJourneys(from: String, to: String): Result<List<Journey>> {
        return try {
            val response = routingApi.getJourneys(from, to)
            Result.success(response.journeys?.map { it.toDomain() } ?: emptyList())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getStopHints(query: String): List<Stop> {
        val allStops = stopDao.getAllStops()
        return allStops
            .filter { it.stopName.contains(query, ignoreCase = true) }
            .take(5)
            .map { it.toDto().toDomain() } // Reusing existing DTO/Domain mapping logic
    }

    fun getSavedAddressesFlow(): Flow<List<SavedAddress>> {
        return savedAddressDao.getAllSavedAddressesFlow().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    suspend fun saveAddress(address: SavedAddress) {
        savedAddressDao.insertAddress(address.toEntity())
    }

    suspend fun getAddressByType(type: String): SavedAddress? {
        return savedAddressDao.getAddressByType(type)?.toDomain()
    }
}
