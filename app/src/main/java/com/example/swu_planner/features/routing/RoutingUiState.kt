package com.example.swu_planner.features.routing

import com.example.swu_planner.data.model.Journey
import com.example.swu_planner.data.model.SavedAddress
import com.example.swu_planner.data.model.Stop

/**
 * Data class representing the UI state for the routing/trip planner feature.
 */
data class RoutingUiState(
    val fromQuery: String = "",
    val toQuery: String = "",
    val fromLocation: String? = null,
    val toLocation: String? = null,
    val fromHints: List<Stop> = emptyList(),
    val toHints: List<Stop> = emptyList(),
    val journeys: List<Journey> = emptyList(),
    val savedAddresses: List<SavedAddress> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val addressDraft: AddressDraft = AddressDraft()
)

/**
 * Holds temporary state while setting or editing a saved address.
 */
data class AddressDraft(
    val type: String? = null, // "home", "work", or "other"
    val query: String = "",
    val hints: List<Stop> = emptyList(),
    val name: String = "",
    val icon: String = "place",
    val latitude: Double? = null,
    val longitude: Double? = null,
    val editingAddress: SavedAddress? = null
)
