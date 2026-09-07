package com.example.swu_planner.features.routing

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.swu_planner.data.model.SavedAddress
import com.example.swu_planner.data.repository.RoutingRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RoutingViewModel @Inject constructor(
    private val repository: RoutingRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(RoutingUiState())
    val uiState: StateFlow<RoutingUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            repository.getSavedAddressesFlow().collect { addresses ->
                _uiState.update { it.copy(savedAddresses = addresses) }
            }
        }
    }

    fun onFromQueryChanged(query: String) {
        _uiState.update { it.copy(fromQuery = query) }
        viewModelScope.launch {
            val hints = repository.getStopHints(query)
            _uiState.update { it.copy(fromHints = hints) }
        }
    }

    fun onToQueryChanged(query: String) {
        _uiState.update { it.copy(toQuery = query) }
        viewModelScope.launch {
            val hints = repository.getStopHints(query)
            _uiState.update { it.copy(toHints = hints) }
        }
    }

    fun findRoutes() {
        val from = _uiState.value.fromQuery
        val to = _uiState.value.toQuery
        if (from.isBlank() || to.isBlank()) return

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            repository.findJourneys(from, to).fold(
                onSuccess = { journeys ->
                    _uiState.update { it.copy(journeys = journeys, isLoading = false) }
                },
                onFailure = { error ->
                    _uiState.update { it.copy(error = error.message, isLoading = false) }
                }
            )
        }
    }

    fun useCurrentLocationAsDeparture(latitude: Double, longitude: Double) {
        val latLngString = "$latitude,$longitude"
        _uiState.update { it.copy(fromQuery = "Current Location ($latLngString)", fromHints = emptyList()) }
    }

    fun useSavedAddress(address: SavedAddress, isDeparture: Boolean) {
        if (isDeparture) {
            _uiState.update { it.copy(fromQuery = address.name, fromHints = emptyList()) }
        } else {
            _uiState.update { it.copy(toQuery = address.name, toHints = emptyList()) }
        }
    }

    fun saveCurrentAddress(type: String, name: String, address: String, lat: Double, lon: Double) {
        viewModelScope.launch {
            repository.saveAddress(SavedAddress(type, name, address, lat, lon))
        }
    }
}
