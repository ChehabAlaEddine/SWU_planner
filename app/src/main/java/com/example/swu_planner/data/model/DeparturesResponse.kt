package com.example.swu_planner.data.model

data class DeparturesResponse(
    val StopPassage: StopPassageData
)

data class StopPassageData(
    val ServiceCategory: String,
    val ContentScope: String,
    val Limit: Int,
    val State: String,
    val CurrentTimestamp: String,
    val PassageData: List<Departure>? = null  // Only present when State is "OK"
)