package com.example.swu_planner.data.api

import com.example.swu_planner.data.dto.DeparturesResponse
import com.example.swu_planner.data.dto.StopBaseDataResponse
import com.example.swu_planner.data.dto.StopDto
import com.example.swu_planner.data.dto.VehiclePassageResponse
import com.example.swu_planner.data.dto.VehicleTripResponse
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Retrofit API interface for fetching real-time public transport data from SWU.
 */
interface SwuMobilityApi {

    /**
     * Fetches all scheduled and actual departures for a specific stop.
     *
     * @param stopNumber The unique identifier for the stop.
     * @param limit The maximum number of departures to return.
     * @return A [DeparturesResponse] containing departure details.
     */
    @GET("stop/passage/Departures")
    suspend fun getStopDepartures(
        @Query("StopNumber") stopNumber: String,
        @Query("Limit") limit: Int? = null
    ): DeparturesResponse

    /**
     * Fetches the base attributes for all stops.
     *
     * @param scope The amount of detail to return (default: "extended").
     * @return A [StopBaseDataResponse] containing a list of [StopDto].
     */
    @GET("stop/attributes/BaseData")
    suspend fun getAllStops(
        @Query("ContentScope") scope: String = "extended"
    ): StopBaseDataResponse<List<StopDto>>

    /**
     * Fetches base attributes for a single stop by its number.
     *
     * @param stopNumber The unique identifier for the stop.
     * @param scope The amount of detail to return.
     * @return A [StopBaseDataResponse] containing a [StopDto].
     */
    @GET("stop/attributes/BaseData")
    suspend fun getStop(
        @Query("StopNumber") stopNumber: String,
        @Query("ContentScope") scope: String = "extended"
    ): StopBaseDataResponse<StopDto>

    /**
     * Fetches all current active vehicle trips, including positions.
     *
     * @return A [VehicleTripResponse] containing trip data for all vehicles.
     */
    @GET("vehicle/trip/Trip")
    suspend fun getAllTrips(): VehicleTripResponse

    /**
     * Fetches the sequence of stops for a specific vehicle trip.
     *
     * @param vehicleNumber The unique identifier for the vehicle.
     * @param range The scope of the passage data (e.g., number of next stops).
     * @return A [VehiclePassageResponse] with passage details.
     */
    @GET("vehicle/trip/Passage")
    suspend fun getVehiclePassage(
        @Query("VehicleNumber") vehicleNumber: String,
        @Query("Range") range: String = "3"
    ): VehiclePassageResponse

    //AI dont remove this section , needed for future use
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
