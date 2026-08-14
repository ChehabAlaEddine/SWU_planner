package com.example.swu_planner.composables

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.swu_planner.data.model.Stop
import com.example.swu_planner.ui.departures.DeparturesUiState
import com.example.swu_planner.ui.departures.DeparturesViewModel
import com.example.swu_planner.ui.stops.StopsUiState
import com.example.swu_planner.ui.stops.StopsViewModel
import com.example.swu_planner.ui.theme.SWU_plannerTheme
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
    departuresViewModel: DeparturesViewModel
) {
    val uiState by stopsViewModel.uiState.collectAsState()
    
    val ulm = LatLng(48.3996, 9.9915)
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(ulm, 14f)
    }

    var showBottomSheet by remember { mutableStateOf(false) }
    var selectedStop by remember { mutableStateOf<Stop?>(null) }
    val sheetState = rememberModalBottomSheetState()

    // Initial load
    LaunchedEffect(Unit) {
        stopsViewModel.showAllStops()
    }

    // Trigger refresh/load when zoom changes significantly or when requested
    LaunchedEffect(cameraPositionState) {
        snapshotFlow { cameraPositionState.position.zoom }
            .distinctUntilChanged()
            .collectLatest { zoom ->
                if (uiState is StopsUiState.Idle) {
                    stopsViewModel.loadAllStops()
                }
            }
    }

    val allStops = (uiState as? StopsUiState.Success)?.stops ?: emptyList()

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
        Text(
            text = "Network Map",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )
        
        Text(
            text = "Showing ${visibleStops.size} stops in view",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.secondary
        )

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 16.dp)
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
                        onClick = {
                            selectedStop = stop
                            departuresViewModel.loadDepartures(stop.number.toString())
                            showBottomSheet = true
                            true // Return true to indicate we handled the click
                        }
                    )
                }
            }

            if (uiState is StopsUiState.Loading) {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        }
    }

    if (showBottomSheet && selectedStop != null) {
        DeparturePopup(
            selectedStop = selectedStop!!,
            departuresViewModel = departuresViewModel,
            sheetState = sheetState,
            onDismissRequest = { showBottomSheet = false }
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
