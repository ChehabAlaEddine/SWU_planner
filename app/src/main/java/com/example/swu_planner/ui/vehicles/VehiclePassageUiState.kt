package com.example.swu_planner.ui.vehicles

import com.example.swu_planner.data.model.VehiclePassage

sealed class VehiclePassageUiState {
    data object Idle : VehiclePassageUiState()
    data object Loading : VehiclePassageUiState()
    data class Success(val passages: List<VehiclePassage>) : VehiclePassageUiState()
    data class Error(val message: String) : VehiclePassageUiState()
}
