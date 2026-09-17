package com.example.swu_planner.data.api

import com.example.swu_planner.data.dto.EfaJourneysResponse
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Retrofit API interface for fetching routing/journey information from the EFA system.
 */
interface RoutingApi {
    /**
     * Fetches potential journeys between an origin and a destination.
     *
     * @param origin The starting point name.
     * @param originType The type of the origin (e.g., "stop", "address").
     * @param destination The ending point name.
     * @param destinationType The type of the destination.
     * @param outputFormat The desired response format (default: "JSON").
     * @param depArr Whether the time refers to departure ("dep") or arrival ("arr").
     * @return An [EfaJourneysResponse] containing trip details.
     */
    @GET("XSLT_TRIP_REQUEST2")
    suspend fun getJourneys(
        @Query("name_origin") origin: String,
        @Query("type_origin") originType: String,
        @Query("name_destination") destination: String,
        @Query("type_destination") destinationType: String,
        @Query("outputFormat") outputFormat: String = "JSON",
        @Query("itdTripDateTimeDepArr") depArr: String = "dep"
    ): EfaJourneysResponse
}
