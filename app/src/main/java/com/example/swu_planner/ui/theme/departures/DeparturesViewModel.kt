package com.example.swu_planner.ui.theme.departures

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.swu_planner.data.api.SwuApiClient
import com.example.swu_planner.data.model.Departure
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class DeparturesViewModel : ViewModel() {

    private val _departures = MutableStateFlow<List<Departure>>(emptyList())
    val departures: StateFlow<List<Departure>> = _departures

    fun loadDepartures(stopNumber: String) {
        print("loadDepartures")
        viewModelScope.launch {
            try {
                val response = SwuApiClient.api.getStopDepartures(stopNumber, limit = 10)
                Log.d("DeparturesViewModel", "Response: $response")
                _departures.value = response.Departures ?:
                List(1) { Departure("0","0","0", "0", "0") }
            } catch (e: Exception) {
                // handle network/parsing error, e.g. update UI error state
            }
        }
    }


}