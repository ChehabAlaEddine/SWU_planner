package com.example.swu_planner.features.routing

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.swu_planner.data.model.SavedAddress
import com.example.swu_planner.data.model.Stop
import com.example.swu_planner.data.repository.RoutingRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel for the routing/trip planner feature.
 *
 * Manages journey searches, stop suggestions, and saved locations.
 */
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

    private fun updateDraft(transform: (AddressDraft) -> AddressDraft) {
        _uiState.update { it.copy(addressDraft = transform(it.addressDraft)) }
    }

    private fun fetchHints(query: String, update: (List<Stop>) -> Unit) {
        viewModelScope.launch {
            val hints = repository.getStopHints(query)
            update(hints)
        }
    }

    /**
     * Triggers routing from a specific location to a saved address.
     */
    fun routeToSavedAddress(address: SavedAddress, currentLat: Double, currentLon: Double) {
        _uiState.update {
            it.copy(
                fromQuery = "Your location",
                fromLocation = "$currentLat,$currentLon",
                toQuery = address.name,
                toLocation = "${address.latitude},${address.longitude}",
                fromHints = emptyList(),
                toHints = emptyList()
            )
        }
        findRoutes()
    }

    /**
     * Handles the click on Home or Work buttons.
     */
    fun onSavedAddressActionButtonClicked(type: String) {
        val saved = _uiState.value.savedAddresses.find { it.type == type }
        if (saved == null) {
            updateDraft { it.copy(type = type, query = "", hints = emptyList()) }
        }
    }

    /**
     * Opens the UI to add a new custom address.
     */
    fun onAddCustomAddressClicked() {
        updateDraft { 
            it.copy(
                type = "other", 
                query = "", 
                hints = emptyList(),
                name = "",
                icon = "place"
            ) 
        }
    }

    /**
     * Updates the query for setting a home/work/other address.
     */
    fun onAddressSettingQueryChanged(query: String) {
        updateDraft { 
            it.copy(
                query = query,
                latitude = null,
                longitude = null,
                editingAddress = it.editingAddress?.copy(latitude = 0.0, longitude = 0.0)
            ) 
        }
        fetchHints(query) { hints ->
            updateDraft { it.copy(hints = hints) }
        }
    }

    fun onSettingNameChanged(name: String) = updateDraft { it.copy(name = name) }
    fun onSettingIconChanged(icon: String) = updateDraft { it.copy(icon = icon) }
    fun onEditNameChanged(name: String) = updateDraft { it.copy(name = name) }
    fun onEditIconChanged(icon: String) = updateDraft { it.copy(icon = icon) }

    /**
     * Saves the selected stop and updates the UI state without dismissing if editing.
     */
    fun onAddressSettingStopSelected(stop: Stop) {
        val draft = _uiState.value.addressDraft
        if (draft.editingAddress != null) {
            updateDraft {
                it.copy(
                    editingAddress = it.editingAddress?.copy(
                        address = stop.name,
                        latitude = stop.latitude, 
                        longitude = stop.longitude
                    ),
                    query = stop.name,
                    hints = emptyList()
                )
            }
            return
        }

        val type = draft.type ?: return
        
        updateDraft {
            it.copy(
                query = stop.name,
                hints = emptyList(),
                latitude = stop.latitude,
                longitude = stop.longitude
            )
        }
        
        if (type == "home" || type == "work") {
            viewModelScope.launch {
                repository.saveAddress(SavedAddress(type, stop.name, stop.name, stop.latitude, stop.longitude, "place"))
                updateDraft { AddressDraft() }
            }
        }
    }

    /**
     * Saves a new custom address.
     */
    fun saveNewCustomAddress() {
        val draft = _uiState.value.addressDraft
        if (draft.query.isBlank()) return
        
        val lat = draft.latitude ?: return
        val lon = draft.longitude ?: return
        val finalName = if (draft.name.isBlank()) draft.query else draft.name
        
        viewModelScope.launch {
            val finalType = "other_${System.currentTimeMillis()}"
            repository.saveAddress(SavedAddress(finalType, finalName, draft.query, lat, lon, draft.icon))
            updateDraft { AddressDraft() }
        }
    }

    fun cancelAddressSetting() = updateDraft { AddressDraft() }

    /**
     * Handles long click on a saved address to open edit UI.
     */
    fun onSavedAddressLongClicked(type: String) {
        val address = _uiState.value.savedAddresses.find { it.type == type } ?: return
        updateDraft { 
            it.copy(
                editingAddress = address, 
                name = address.name,
                icon = address.iconName,
                query = address.address,
                hints = emptyList()
            ) 
        }
    }

    /**
     * Saves the edited address.
     */
    fun saveEditedAddress() {
        val draft = _uiState.value.addressDraft
        val currentEditing = draft.editingAddress ?: return
        
        if (draft.query.isBlank()) return
        val finalName = if (draft.name.isBlank()) draft.query else draft.name
        
        viewModelScope.launch {
            repository.saveAddress(currentEditing.copy(
                name = finalName,
                address = draft.query,
                iconName = draft.icon
            ))
            updateDraft { AddressDraft() }
        }
    }

    /**
     * Deletes the address being edited.
     */
    fun deleteEditingAddress() {
        val draft = _uiState.value.addressDraft
        val currentEditing = draft.editingAddress ?: return
        viewModelScope.launch {
            repository.deleteAddress(currentEditing.type)
            updateDraft { AddressDraft() }
        }
    }

    fun cancelEditingAddress() = updateDraft { AddressDraft() }

    /**
     * Updates the origin search query and fetches hints.
     */
    fun onFromQueryChanged(query: String) {
        _uiState.update { it.copy(fromQuery = query, fromLocation = null) }
        fetchHints(query) { hints ->
            _uiState.update { it.copy(fromHints = hints) }
        }
    }

    /**
     * Updates the destination search query and fetches hints.
     */
    fun onToQueryChanged(query: String) {
        _uiState.update { it.copy(toQuery = query, toLocation = null) }
        fetchHints(query) { hints ->
            _uiState.update { it.copy(toHints = hints) }
        }
    }

    /**
     * Initiates a search for routes between the selected origin and destination.
     */
    fun findRoutes() {
        val state = _uiState.value
        val from = state.fromLocation ?: state.fromQuery
        val to = state.toLocation ?: state.toQuery
        if (from.isBlank() || to.isBlank()) return

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            repository.findJourneys(from, to).fold(
                onSuccess = { journeys ->
                    _uiState.update { it.copy(journeys = journeys, isLoading = false) }
                },
                onFailure = { error ->
                    val errorMessage = if (error.message?.contains("503") == true) {
                        "The routing service is currently overloaded. Please try again in a few seconds."
                    } else {
                        error.message ?: "An unexpected error occurred"
                    }
                    _uiState.update { it.copy(error = errorMessage, isLoading = false) }
                }
            )
        }
    }

    /**
     * Swaps the origin and destination locations.
     */
    fun swapLocations() {
        _uiState.update { 
            it.copy(
                fromQuery = it.toQuery,
                toQuery = it.fromQuery,
                fromLocation = it.toLocation,
                toLocation = it.fromLocation,
                fromHints = emptyList(),
                toHints = emptyList()
            ) 
        }
    }

    /**
     * Sets the current device location as the departure point.
     */
    fun useCurrentLocationAsDeparture(latitude: Double, longitude: Double) {
        _uiState.update { 
            it.copy(
                fromQuery = "Your location",
                fromLocation = "$latitude,$longitude",
                fromHints = emptyList()
            ) 
        }
    }

    /**
     * Sets a saved address as either the origin or destination.
     */
    fun useSavedAddress(address: SavedAddress, isDeparture: Boolean) {
        _uiState.update { 
            if (isDeparture) {
                it.copy(
                    fromQuery = address.name,
                    fromLocation = "${address.latitude},${address.longitude}",
                    fromHints = emptyList()
                )
            } else {
                it.copy(
                    toQuery = address.name,
                    toLocation = "${address.latitude},${address.longitude}",
                    toHints = emptyList()
                )
            }
        }
    }

    /**
     * Saves an address for future quick access.
     */
    fun saveCurrentAddress(type: String, name: String, address: String, lat: Double, lon: Double) {
        viewModelScope.launch {
            repository.saveAddress(SavedAddress(type, name, address, lat, lon))
        }
    }
}
