package com.example.swu_planner.features.routing

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import com.example.swu_planner.data.model.Journey
import com.example.swu_planner.data.model.SavedAddress
import com.example.swu_planner.data.model.Stop
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RoutingScreen(viewModel: RoutingViewModel) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val fusedLocationClient = remember { LocationServices.getFusedLocationProviderClient(context) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Spacer(modifier = Modifier.height(32.dp))
        Text(
            text = "Trip Planner",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(16.dp))

        // Departure Input
        AutocompleteField(
            label = "From",
            value = uiState.fromQuery,
            onValueChange = { viewModel.onFromQueryChanged(it) },
            hints = uiState.fromHints,
            onHintSelected = { viewModel.useSavedAddress(SavedAddress("stop", it.name, "", it.latitude, it.longitude), true) },
            trailingIcon = {
                IconButton(onClick = {
                    scope.launch {
                        if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                            val location = fusedLocationClient.lastLocation.await()
                            if (location != null) {
                                viewModel.useCurrentLocationAsDeparture(location.latitude, location.longitude)
                            }
                        }
                    }
                }) {
                    Icon(Icons.Default.MyLocation, contentDescription = "Current Location")
                }
            }
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Destination Input
        AutocompleteField(
            label = "To",
            value = uiState.toQuery,
            onValueChange = { viewModel.onToQueryChanged(it) },
            hints = uiState.toHints,
            onHintSelected = { viewModel.useSavedAddress(SavedAddress("stop", it.name, "", it.latitude, it.longitude), false) }
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Quick Actions (Home/Work)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            SavedAddressButton("Home", Icons.Default.Home, uiState.savedAddresses.find { it.type == "home" }) {
                viewModel.useSavedAddress(it, false)
            }
            SavedAddressButton("Work", Icons.Default.Work, uiState.savedAddresses.find { it.type == "work" }) {
                viewModel.useSavedAddress(it, false)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = { viewModel.findRoutes() },
            modifier = Modifier.fillMaxWidth(),
            enabled = !uiState.isLoading
        ) {
            if (uiState.isLoading) {
                CircularProgressIndicator(modifier = Modifier.size(24.dp), color = Color.White)
            } else {
                Text("Find Routes")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (uiState.error != null) {
            Text(text = uiState.error!!, color = MaterialTheme.colorScheme.error)
        }

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.weight(1f)
        ) {
            items(uiState.journeys) { journey ->
                RouteItem(journey)
            }
        }
    }
}

@Composable
fun AutocompleteField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    hints: List<Stop>,
    onHintSelected: (Stop) -> Unit,
    trailingIcon: @Composable (() -> Unit)? = null
) {
    Column {
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            label = { Text(label) },
            modifier = Modifier.fillMaxWidth(),
            trailingIcon = trailingIcon
        )
        if (hints.isNotEmpty()) {
            Card(
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                hints.forEach { stop ->
                    Text(
                        text = stop.name,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onHintSelected(stop) }
                            .padding(12.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun SavedAddressButton(
    label: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    address: SavedAddress?,
    onClick: (SavedAddress) -> Unit
) {
    OutlinedButton(
        onClick = { address?.let { onClick(it) } },
        modifier = Modifier.height(48.dp),
        enabled = address != null
    ) {
        Icon(icon, contentDescription = null, modifier = Modifier.size(18.dp))
        Spacer(modifier = Modifier.width(8.dp))
        Text(label)
    }
}

@Composable
fun RouteItem(journey: Journey) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "${journey.departureTime} → ${journey.arrivalTime}",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = journey.duration,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.secondary
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            // Summary of legs
            Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                journey.legs.forEachIndexed { index, leg ->
                    if (leg.lineName != null) {
                        Surface(
                            color = MaterialTheme.colorScheme.primaryContainer,
                            shape = androidx.compose.foundation.shape.RoundedCornerShape(4.dp)
                        ) {
                            Text(
                                text = leg.lineName,
                                modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp),
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    } else {
                        Icon(Icons.Default.DirectionsWalk, contentDescription = null, modifier = Modifier.size(16.dp))
                    }
                    if (index < journey.legs.size - 1) {
                        Icon(Icons.Default.ChevronRight, contentDescription = null, modifier = Modifier.size(16.dp))
                    }
                }
            }
        }
    }
}
