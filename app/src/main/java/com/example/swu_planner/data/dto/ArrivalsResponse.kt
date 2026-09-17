package com.example.swu_planner.data.dto

import com.example.swu_planner.data.model.Departure

/**
 * Data Transfer Object for vehicle arrivals at a stop.
 *
 * @property Departures The list of upcoming arrivals/departures.
 */
data class ArrivalsResponse(
    val Departures: List<Departure>?
)
