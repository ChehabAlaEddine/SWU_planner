package com.example.swu_planner.ui.vehicles

import com.example.swu_planner.data.model.Trip

sealed class VehicleUiState {
    data object Idle : VehicleUiState()
    data object Loading : VehicleUiState()
    data class Success(val trips: List<Trip>) : VehicleUiState()
    data class Error(val message: String) : VehicleUiState()
}
