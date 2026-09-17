package com.example.swu_planner.features.map

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.repeatOnLifecycle
import com.example.swu_planner.R
import com.example.swu_planner.core.theme.SWU_plannerTheme
import com.example.swu_planner.core.utils.bitmapDescriptorFromVector
import com.example.swu_planner.core.utils.createVehicleIcon
import com.example.swu_planner.data.model.Stop
import com.example.swu_planner.data.model.Trip
import com.example.swu_planner.features.departures.DeparturePopup
import com.example.swu_planner.features.departures.DeparturesViewModel
import com.example.swu_planner.features.stops.StopsUiState
import com.example.swu_planner.features.stops.StopsViewModel
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.MapsComposeExperimentalApi
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

/**
 * The main map screen of the application.
 *
 * Displays stops and active vehicle trips on a Google Map.
 * Provides functionality to view departures for a stop and passage details for a vehicle.
 *
 * @param stopsViewModel ViewModel for stop data.
 * @param departuresViewModel ViewModel for departure data.
 * @param vehicleViewModel ViewModel for vehicle trip data.
 * @param contentPadding Padding values from the Scaffold to handle system bars.
 */
@SuppressLint("MissingPermission")
@OptIn(ExperimentalMaterial3Api::class, MapsComposeExperimentalApi::class)
@Composable
fun MapScreen(
    stopsViewModel: StopsViewModel,
    departuresViewModel: DeparturesViewModel,
    vehicleViewModel: VehicleViewModel,
    contentPadding: PaddingValues = PaddingValues(0.dp)
) {
    val uiState by stopsViewModel.uiState.collectAsState()
    val vehicleUiState by vehicleViewModel.uiState.collectAsState()
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

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

    var isLocationPermissionGranted by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        )
    }

    val locationPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        isLocationPermissionGranted = permissions.values.contains(true)
    }

    val fusedLocationClient = remember {
        LocationServices.getFusedLocationProviderClient(context)
    }

    suspend fun moveToCurrentLocation() {
        if (isLocationPermissionGranted) {
            try {
                val location = fusedLocationClient.lastLocation.await()
                if (location != null) {
                    val latLng = LatLng(location.latitude, location.longitude)
                    cameraPositionState.animate(
                        CameraUpdateFactory.newLatLngZoom(latLng, 15f)
                    )
                }
            } catch (e: Exception) {
                Log.e("MapScreen", "Error getting location", e)
            }
        } else {
            locationPermissionLauncher.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
        }
    }

    var showStopBottomSheet by remember { mutableStateOf(false) }
    var selectedStop by remember { mutableStateOf<Stop?>(null) }
    val stopSheetState = rememberModalBottomSheetState()

    var showVehicleBottomSheet by remember { mutableStateOf(false) }
    var selectedTrip by remember { mutableStateOf<Trip?>(null) }
    val vehicleSheetState = rememberModalBottomSheetState()

    val stopIcon = remember(context) {
        bitmapDescriptorFromVector(context, R.drawable.ic_stop_marker)
    }

    // Initial load
    LaunchedEffect(Unit) {
        stopsViewModel.showAllStops()
        vehicleViewModel.loadActiveTrips()
    }

    // Periodically refresh vehicle trips when active and no popup is shown
    val lifecycleOwner = LocalLifecycleOwner.current
    LaunchedEffect(lifecycleOwner, showStopBottomSheet, showVehicleBottomSheet) {
        lifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
            while (true) {
                if (!showStopBottomSheet && !showVehicleBottomSheet) {
                    vehicleViewModel.loadActiveTrips()
                }
                delay(15000)
            }
        }
    }

    val allStops = (uiState as? StopsUiState.Success)?.stops ?: emptyList()

    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        GoogleMap(
            modifier = Modifier.fillMaxSize(),
            cameraPositionState = cameraPositionState,
            properties = MapProperties(
                isMyLocationEnabled = isLocationPermissionGranted
            ),
            uiSettings = MapUiSettings(
                myLocationButtonEnabled = false, // We use our custom FAB
                zoomControlsEnabled = true
            ),
            contentPadding = contentPadding
        ) {
            allStops.forEach { stop ->
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

            FloatingActionButton(
                onClick = {
                    coroutineScope.launch {
                        moveToCurrentLocation()
                    }
                },
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(contentPadding)
                    .consumeWindowInsets(contentPadding)
                    .padding(start=16.dp, bottom=32.dp),
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                contentColor = MaterialTheme.colorScheme.onPrimaryContainer
            ) {
                Icon(
                    imageVector = Icons.Default.MyLocation,
                    contentDescription = "My Location"
                )
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

/**
 * A preview composable for the [MapScreen].
 */
@Preview(showBackground = true)
@Composable
fun MapScreenPreview() {
    SWU_plannerTheme {
        Text("Map Screen Preview")
    }
}
