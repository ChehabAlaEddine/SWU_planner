package com.example.swu_planner.ui.stops

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.swu_planner.data.model.mapper.toDomain
import com.example.swu_planner.data.repository.StopsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class StopsViewModel(
    private val repository: StopsRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<StopsUiState>(StopsUiState.Idle)
    val uiState: StateFlow<StopsUiState> = _uiState

    private var lastLoadedStops: List<com.example.swu_planner.data.model.Stop> = emptyList()

    fun loadAllStops() {
        viewModelScope.launch {
            _uiState.value = StopsUiState.Loading

            val result = repository.getAllStops()
            _uiState.value = result.fold(
                onSuccess = { stopDtos ->
                    val domainStops = stopDtos.map { it.toDomain() }
                    lastLoadedStops = domainStops
                    if (domainStops.isEmpty()) {
                        Log.d("StopsViewModel", "No stops available")
                        StopsUiState.Empty("No stops available")
                    } else {
                        Log.d("StopsViewModel", "Stops loaded successfully: ${domainStops.size}")
                        StopsUiState.Success(domainStops)
                    }
                },
                onFailure = {
                    Log.e("StopsViewModel", "Error loading stops", it)
                    StopsUiState.Error(it.message ?: "Unknown error")
                }
            )
        }
    }

    fun loadStop(stopNumber: String) {
        viewModelScope.launch {
            _uiState.value = StopsUiState.Loading

            val result = repository.getStop(stopNumber)
            _uiState.value = result.fold(
                onSuccess = { stopDto ->
                    val domainStop = stopDto.toDomain()
                    Log.d("StopsViewModel", "Stop loaded successfully: ${domainStop.name}")
                    StopsUiState.StopDetail(domainStop)
                },
                onFailure = {
                    Log.e("StopsViewModel", "Error loading stop $stopNumber", it)
                    StopsUiState.Error(it.message ?: "Unknown error")
                }
            )
        }
    }

    fun showAllStops() {
        if (lastLoadedStops.isNotEmpty()) {
            _uiState.value = StopsUiState.Success(lastLoadedStops)
        } else {
            loadAllStops()
        }
    }
}
