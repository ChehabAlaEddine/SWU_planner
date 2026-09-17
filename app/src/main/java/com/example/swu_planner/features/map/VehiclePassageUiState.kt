package com.example.swu_planner.features.map

import com.example.swu_planner.data.model.VehiclePassage

/**
 * Sealed class representing the UI states for vehicle passage details.
 */
sealed class VehiclePassageUiState {
    data object Idle : VehiclePassageUiState()
    data object Loading : VehiclePassageUiState()
    data class Success(val passages: List<VehiclePassage>) : VehiclePassageUiState()
    data class Error(val message: String) : VehiclePassageUiState()
}
