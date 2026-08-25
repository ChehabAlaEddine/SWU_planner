package com.example.swu_planner.features.map

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.swu_planner.data.repository.VehicleRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class VehicleViewModel @Inject constructor(private val repository: VehicleRepository) : ViewModel() {

    private val _uiState = MutableStateFlow<VehicleUiState>(VehicleUiState.Idle)
    val uiState: StateFlow<VehicleUiState> = _uiState

    private val _passageUiState = MutableStateFlow<VehiclePassageUiState>(VehiclePassageUiState.Idle)
    val passageUiState: StateFlow<VehiclePassageUiState> = _passageUiState

    fun loadActiveTrips() {
        viewModelScope.launch {
            _uiState.value = VehicleUiState.Loading
            val result = repository.getActiveTrips()
            _uiState.value = result.fold(
                onSuccess = { trips ->
                    VehicleUiState.Success(trips)
                },
                onFailure = {
                    VehicleUiState.Error(it.message ?: "Unknown error")
                }
            )
        }
    }

    fun loadVehiclePassage(vehicleNumber: String) {
        viewModelScope.launch {
            _passageUiState.value = VehiclePassageUiState.Loading
            val result = repository.getVehiclePassage(vehicleNumber)
            _passageUiState.value = result.fold(
                onSuccess = { passages ->
                    VehiclePassageUiState.Success(passages)
                },
                onFailure = {
                    VehiclePassageUiState.Error(it.message ?: "Unknown error")
                }
            )
        }
    }
    
    fun clearPassage() {
        _passageUiState.value = VehiclePassageUiState.Idle
    }
}
