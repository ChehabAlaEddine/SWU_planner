package com.example.swu_planner.features.routing

import com.example.swu_planner.data.model.Journey
import com.example.swu_planner.data.model.SavedAddress
import com.example.swu_planner.data.model.Stop

data class RoutingUiState(
    val fromQuery: String = "",
    val toQuery: String = "",
    val fromHints: List<Stop> = emptyList(),
    val toHints: List<Stop> = emptyList(),
    val journeys: List<Journey> = emptyList(),
    val savedAddresses: List<SavedAddress> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)
