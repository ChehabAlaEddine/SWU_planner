package com.example.swu_planner.data.api

import com.example.swu_planner.data.model.DeparturesResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface SwuMobilityApi {

    @GET("stop/passage/Departures")
    suspend fun getStopDepartures(
        @Query("StopNumber") stopNumber: String,
        @Query("Limit") limit: Int? = null
    ): DeparturesResponse

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