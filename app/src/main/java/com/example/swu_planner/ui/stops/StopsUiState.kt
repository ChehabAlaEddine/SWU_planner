package com.example.swu_planner.ui.stops

import com.example.swu_planner.data.model.Stop

sealed class StopsUiState {
    data object Idle : StopsUiState()
    data object Loading : StopsUiState()
    data class Success(val stops: List<Stop>) : StopsUiState()
    data class StopDetail(val stop: Stop) : StopsUiState()
    data class Empty(val message: String) : StopsUiState()
    data class Error(val message: String) : StopsUiState()
}
