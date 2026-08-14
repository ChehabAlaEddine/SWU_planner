package com.example.swu_planner.data.api

import com.example.swu_planner.data.model.DeparturesResponse
import com.example.swu_planner.data.model.StopBaseDataResponse
import com.example.swu_planner.data.model.StopDto
import retrofit2.http.GET
import retrofit2.http.Query

interface SwuMobilityApi {

    // get all departures for given stop
    @GET("stop/passage/Departures")
    suspend fun getStopDepartures(
        @Query("StopNumber") stopNumber: String,
        @Query("Limit") limit: Int? = null
    ): DeparturesResponse

    // get all stops
    @GET("stop/attributes/BaseData")
    suspend fun getAllStops(
        @Query("ContentScope") scope: String = "extended"
    ): StopBaseDataResponse<List<StopDto>>

    // get a single stop by number:
    @GET("stop/attributes/BaseData")
    suspend fun getStop(
        @Query("StopNumber") stopNumber: String,
        @Query("ContentScope") scope: String = "extended"
    ): StopBaseDataResponse<StopDto>

    /*
    @GET("stop/passage/Arrivals")
    suspend fun getStopArrivals(
        @Query("StopNumber") stopNumber: String,
        @Query("Limit") limit: Int? = null
    ): ArrivalsResponse

    @GET("stop/attributes/BaseData")
    suspend fun getStopBaseData(
        @Query("ContentScope") scope: String? = null,
        @Query("StopNumber") stopNumber: String? = null
    ): StopBaseDataResponse

    @GET("route/attributes/BaseData")
    suspend fun getRouteBaseData(
        @Query("ContentScope") scope: String? = null,
        @Query("RouteNumber") routeNumber: String? = null
    ): RouteBaseDataResponse

    @GET("vehicle/trip/Passage")
    suspend fun getVehiclePassage(
        @Query("VehicleNumber") vehicleNumber: String,
        @Query("Range") range: String? = null
    ): VehiclePassageResponse

     */
}