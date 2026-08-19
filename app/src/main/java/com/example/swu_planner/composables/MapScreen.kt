package com.example.swu_planner.composables

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.swu_planner.R
import com.example.swu_planner.data.model.Stop
import com.example.swu_planner.data.model.Trip
import com.example.swu_planner.ui.departures.DeparturesViewModel
import com.example.swu_planner.ui.stops.StopsUiState
import com.example.swu_planner.ui.stops.StopsViewModel
import com.example.swu_planner.ui.vehicles.VehicleUiState
import com.example.swu_planner.ui.vehicles.VehicleViewModel
import com.example.swu_planner.ui.theme.SWU_plannerTheme
import com.example.swu_planner.utils.bitmapDescriptorFromVector
import com.example.swu_planner.utils.createVehicleIcon
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MapScreen(
    stopsViewModel: StopsViewModel,
    departuresViewModel: DeparturesViewModel,
    vehicleViewModel: VehicleViewModel
) {
    val uiState by stopsViewModel.uiState.collectAsState()
    val vehicleUiState by vehicleViewModel.uiState.collectAsState()

    val allStops = (uiState as? StopsUiState.Success)?.stops ?: emptyList()
    var displayedTrips by remember { mutableStateOf<List<Trip>>(emptyList()) }

    LaunchedEffect(vehicleUiState) {
        if (vehicleUiState is VehicleUiState.Success) {
            displayedTrips = (vehicleUiState as VehicleUiState.Success).trips
        }
    }
    
    val ulm = LatLng(48.3996, 9.9915)
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(ulm, 14f)
    }

    var showStopBottomSheet by remember { mutableStateOf(false) }
    var selectedStop by remember { mutableStateOf<Stop?>(null) }
    val stopSheetState = rememberModalBottomSheetState()

    var showVehicleBottomSheet by remember { mutableStateOf(false) }
    var selectedTrip by remember { mutableStateOf<Trip?>(null) }
    val vehicleSheetState = rememberModalBottomSheetState()

    val context = LocalContext.current
    val stopIcon = remember(context) {
        bitmapDescriptorFromVector(context, R.drawable.ic_stop_marker)
    }

    // Initial load
    LaunchedEffect(Unit) {
        stopsViewModel.showAllStops()
        vehicleViewModel.loadActiveTrips()
    }

    // Periodically refresh vehicle trips
    LaunchedEffect(Unit) {
        while (true) {
            kotlinx.coroutines.delay(15000)
            vehicleViewModel.loadActiveTrips()
        }
    }

    // Trigger refresh/load when zoom changes significantly or when requested
    LaunchedEffect(cameraPositionState) {
        snapshotFlow { cameraPositionState.position.zoom }
            .distinctUntilChanged()
            .collectLatest { _ ->
                if (uiState is StopsUiState.Idle) {
                    stopsViewModel.loadAllStops()
                }
            }
    }

    // Local filtering based on visible region
    val visibleStops = remember(allStops, cameraPositionState.isMoving, cameraPositionState.projection) {
        val bounds = cameraPositionState.projection?.visibleRegion?.latLngBounds
        if (bounds != null) {
            allStops.filter { bounds.contains(LatLng(it.latitude, it.longitude)) }
        } else {
            allStops
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
        ) {
            GoogleMap(
                modifier = Modifier.fillMaxSize(),
                cameraPositionState = cameraPositionState
            ) {
                visibleStops.forEach { stop ->
                    Marker(
                        state = MarkerState(position = LatLng(stop.latitude, stop.longitude)),
                        title = stop.name,
                        snippet = "Stop #${stop.number}",
                        icon = stopIcon,
                        onClick = {
                            selectedStop = stop
                            departuresViewModel.loadDepartures(stop.number.toString())
                            showStopBottomSheet = true
                            true // Return true to indicate we handled the click
                        }
                    )
                }

                // Show vehicle trips
                val vehicleIconsCache = remember { mutableMapOf<String, BitmapDescriptor>() }
                
                displayedTrips.forEach { trip ->
                    if (trip.latitude != null && trip.longitude != null) {
                        val routeName = trip.routeNumber?.rem(100).toString() ?: ""
                        val icon = vehicleIconsCache.getOrPut(routeName) {
                            createVehicleIcon(context, routeName)
                        }
                        
                        Marker(
                            state = MarkerState(position = LatLng(trip.latitude, trip.longitude)),
                            title = "Route ${trip.routeNumber}: ${trip.destination}",
                            snippet = "Vehicle #${trip.vehicleNumber} | Delay: ${trip.deviation}s",
                            icon = icon,
                            onClick = {
                                selectedTrip = trip
                                vehicleViewModel.loadVehiclePassage(trip.vehicleNumber.toString())
                                showVehicleBottomSheet = true
                                true
                            }
                        )
                    }
                }
            }

            if (uiState is StopsUiState.Loading) {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        }
    }

    if (showStopBottomSheet && selectedStop != null) {
        DeparturePopup(
            selectedStop = selectedStop!!,
            departuresViewModel = departuresViewModel,
            sheetState = stopSheetState,
            onDismissRequest = { showStopBottomSheet = false }
        )
    }

    if (showVehicleBottomSheet && selectedTrip != null) {
        VehiclePassagePopup(
            trip = selectedTrip!!,
            viewModel = vehicleViewModel,
            sheetState = vehicleSheetState,
            onDismissRequest = { 
                showVehicleBottomSheet = false
                vehicleViewModel.clearPassage()
            }
        )
    }
}


@Preview(showBackground = true)
@Composable
fun MapScreenPreview() {
    SWU_plannerTheme {
        Text("Map Screen Preview")
    }
}
