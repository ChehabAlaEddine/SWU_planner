package com.example.swu_planner.data.api

import com.example.swu_planner.data.dto.JourneysResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface RoutingApi {
    @GET("journeys")
    suspend fun getJourneys(
        @Query("from") from: String,
        @Query("to") to: String,
        @Query("results") results: Int = 5
    ): JourneysResponse
}
