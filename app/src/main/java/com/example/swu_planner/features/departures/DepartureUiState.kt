package com.example.swu_planner.features.departures

import com.example.swu_planner.data.model.Departure

/**
 * Sealed class representing the different UI states for the departures feature.
 */
sealed class DeparturesUiState {
    data object Idle : DeparturesUiState()
    data object Loading : DeparturesUiState()
    data class Success(val departures: List<Departure>) : DeparturesUiState()
    data class Empty(val message: String) : DeparturesUiState()
    data class Error(val message: String) : DeparturesUiState()
}
