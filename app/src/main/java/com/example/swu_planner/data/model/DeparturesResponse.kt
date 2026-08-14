package com.example.swu_planner.data.model

data class DeparturesResponse(
    //val Departures: List<Departure>?
    val StopPassage: StopPassageData
)

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