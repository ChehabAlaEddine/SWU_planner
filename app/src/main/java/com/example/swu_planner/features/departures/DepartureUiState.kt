package com.example.swu_planner.features.departures

import com.example.swu_planner.data.model.Departure

sealed class DeparturesUiState {
    data object Idle : DeparturesUiState()
    data object Loading : DeparturesUiState()
    data class Success(val departures: List<Departure>) : DeparturesUiState()
    data class Empty(val message: String) : DeparturesUiState()
    data class Error(val message: String) : DeparturesUiState()
}