package com.example.swu_planner.data.dto

import com.example.swu_planner.data.model.Departure

/**
 * Data Transfer Object for the SWU stop departures response.
 */
data class DeparturesResponse(
    val StopPassage: StopPassageData
)

/**
 * Detailed stop passage data containing metadata and a list of departures.
 */
data class StopPassageData(
    val ServiceCategory: String,           // "Departures"
    val UpdateInterval: String,            // "15 seconds"
    val ContentScope: String,              // "Standard"
    val StopNumber: Int,                   // 1000
    val StopCode: String,                  // "ZOB"
    val StopName: String,                  // "ZOB"
    val Limit: Int,                        // 10
    val OrderedBy: String,                 // "DepartureTime ascending"
    val State: String,                     // "ok" or "no stop found"
    val CurrentTimestamp: String,          // ISO 8601 timestamp
    val DepartureData: List<Departure>?    // Array of departures
)
