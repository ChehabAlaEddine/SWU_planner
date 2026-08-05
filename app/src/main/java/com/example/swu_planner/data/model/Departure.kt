package com.example.swu_planner.data.model

data class Departure(
    val StopNumber: String?,
    val RouteNumber: String?,
    val DestinationText: String?,
    val PlannedTime: String?,
    val EstimatedTime: String?,
    val OperatingCompanyName: String?,
    val OperatingCompanyShortName: String?
    // add/adjust fields once you see the real payload
)