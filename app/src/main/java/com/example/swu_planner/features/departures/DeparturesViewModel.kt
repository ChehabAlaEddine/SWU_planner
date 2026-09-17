package com.example.swu_planner.features.departures

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.swu_planner.data.repository.DeparturesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel for the departures feature.
 *
 * Handles fetching and exposing departure data for a specific stop.
 */
@HiltViewModel
class DeparturesViewModel @Inject constructor(
    private val repository: DeparturesRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<DeparturesUiState>(
        DeparturesUiState.Idle
    )
    val uiState: StateFlow<DeparturesUiState> = _uiState

    /**
     * Loads departures for a given stop number.
     *
     * @param stopNumber The identifier for the stop.
     */
    fun loadDepartures(stopNumber: String) {
        viewModelScope.launch {
            _uiState.value = DeparturesUiState.Loading

            val result = repository.getDepartures(stopNumber)
            _uiState.value = result.fold(
                onSuccess = { departures ->
                    if (departures.isEmpty()) {
                        Log.d("DeparturesViewModel", "No departures available")
                        DeparturesUiState.Empty("No departures available")
                    } else {
                        Log.d("DeparturesViewModel", "Departures loaded successfully")
                        Log.d("DeparturesViewModel", departures.toString())
                        DeparturesUiState.Success(departures)
                    }
                },
                onFailure = {
                    DeparturesUiState.Error(it.message ?: "Unknown error")
                }
            )
        }
    }
}
