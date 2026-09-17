package com.example.swu_planner.features.map

import com.example.swu_planner.data.model.Trip

/**
 * Sealed class representing the UI states for active vehicle trips on the map.
 */
sealed class VehicleUiState {
    data object Idle : VehicleUiState()
    data object Loading : VehicleUiState()
    data class Success(val trips: List<Trip>) : VehicleUiState()
    data class Error(val message: String) : VehicleUiState()
}
