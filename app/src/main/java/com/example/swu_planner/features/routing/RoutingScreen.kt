package com.example.swu_planner.features.routing

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.DirectionsWalk
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.example.swu_planner.data.model.Journey
import com.example.swu_planner.data.model.SavedAddress
import com.example.swu_planner.data.model.Stop
import androidx.compose.ui.tooling.preview.Preview
import com.example.swu_planner.data.model.Leg
import com.example.swu_planner.core.theme.SWU_plannerTheme
import com.example.swu_planner.core.utils.getIconForName
import com.example.swu_planner.core.utils.getTransportModeIcon
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

/**
 * Screen that allows users to plan journeys between stops or addresses.
 */
@Composable
fun RoutingScreen(
    viewModel: RoutingViewModel,
    contentPadding: PaddingValues = PaddingValues(0.dp)
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val fusedLocationClient = remember { LocationServices.getFusedLocationProviderClient(context) }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        if (permissions.values.contains(true)) {
            scope.launch {
                val location = fusedLocationClient.getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY, null).await()
                if (location != null) {
                    viewModel.useCurrentLocationAsDeparture(location.latitude, location.longitude)
                }
            }
        }
    }

    // Default to current location on start
    LaunchedEffect(Unit) {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            val location = try {
                fusedLocationClient.getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY, null).await()
                    ?: fusedLocationClient.lastLocation.await()
            } catch (e: Exception) { null }
            if (location != null) {
                viewModel.useCurrentLocationAsDeparture(location.latitude, location.longitude)
            }
        } else {
            permissionLauncher.launch(
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION)
            )
        }
    }

    RoutingContent(
        uiState = uiState,
        onFromQueryChanged = { viewModel.onFromQueryChanged(it) },
        onToQueryChanged = { viewModel.onToQueryChanged(it) },
        onSwapClick = { viewModel.swapLocations() },
        onHintSelected = { stop, isDeparture ->
            viewModel.useSavedAddress(
                SavedAddress("stop", stop.name, "", stop.latitude, stop.longitude),
                isDeparture
            )
        },
        onSavedAddressClick = { type ->
            val address = uiState.savedAddresses.find { it.type == type }
            if (address != null) {
                if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    scope.launch {
                        val location = try {
                            fusedLocationClient.getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY, null).await()
                                ?: fusedLocationClient.lastLocation.await()
                        } catch (e: Exception) { null }
                        if (location != null) {
                            viewModel.routeToSavedAddress(address, location.latitude, location.longitude)
                        }
                    }
                } else {
                    permissionLauncher.launch(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION))
                }
            } else if (type == "home" || type == "work") {
                viewModel.onSavedAddressActionButtonClicked(type)
            }
        },
        onSavedAddressLongClick = { viewModel.onSavedAddressLongClicked(it) },
        onAddCustomAddressClick = { viewModel.onAddCustomAddressClicked() },
        onFindRoutesClick = { viewModel.findRoutes() },
        onCurrentLocationClick = {
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                scope.launch {
                    val location = try {
                        fusedLocationClient.getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY, null).await()
                            ?: fusedLocationClient.lastLocation.await()
                    } catch (e: Exception) { null }
                    if (location != null) {
                        viewModel.useCurrentLocationAsDeparture(location.latitude, location.longitude)
                    }
                }
            } else {
                permissionLauncher.launch(
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION)
                )
            }
        },
        onAddressSettingQueryChanged = { viewModel.onAddressSettingQueryChanged(it) },
        onAddressSettingStopSelected = { viewModel.onAddressSettingStopSelected(it) },
        onSettingNameChanged = { viewModel.onSettingNameChanged(it) },
        onSettingIconChanged = { viewModel.onSettingIconChanged(it) },
        onSaveNewAddress = { viewModel.saveNewCustomAddress() },
        onCancelAddressSetting = { viewModel.cancelAddressSetting() },
        onEditNameChanged = { viewModel.onEditNameChanged(it) },
        onEditIconChanged = { viewModel.onEditIconChanged(it) },
        onSaveEditedAddress = { viewModel.saveEditedAddress() },
        onDeleteEditingAddress = { viewModel.deleteEditingAddress() },
        onCancelEditingAddress = { viewModel.cancelEditingAddress() },
        contentPadding = contentPadding
    )
}

/**
 * Stateless content for the Routing Screen.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RoutingContent(
    uiState: RoutingUiState,
    onFromQueryChanged: (String) -> Unit,
    onToQueryChanged: (String) -> Unit,
    onSwapClick: () -> Unit,
    onHintSelected: (Stop, Boolean) -> Unit,
    onSavedAddressClick: (String) -> Unit,
    onSavedAddressLongClick: (String) -> Unit,
    onAddCustomAddressClick: () -> Unit,
    onFindRoutesClick: () -> Unit,
    onCurrentLocationClick: () -> Unit,
    onAddressSettingQueryChanged: (String) -> Unit = {},
    onAddressSettingStopSelected: (Stop) -> Unit = {},
    onSettingNameChanged: (String) -> Unit = {},
    onSettingIconChanged: (String) -> Unit = {},
    onSaveNewAddress: () -> Unit = {},
    onCancelAddressSetting: () -> Unit = {},
    onEditNameChanged: (String) -> Unit = {},
    onEditIconChanged: (String) -> Unit = {},
    onSaveEditedAddress: () -> Unit = {},
    onDeleteEditingAddress: () -> Unit = {},
    onCancelEditingAddress: () -> Unit = {},
    contentPadding: PaddingValues = PaddingValues(0.dp)
) {
    var selectedJourney by remember { mutableStateOf<Journey?>(null) }
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var showDetailSheet by remember { mutableStateOf(false) }

    AddressSettingDialog(
        uiState = uiState,
        onSettingNameChanged = onSettingNameChanged,
        onSettingIconChanged = onSettingIconChanged,
        onAddressSettingQueryChanged = onAddressSettingQueryChanged,
        onAddressSettingStopSelected = onAddressSettingStopSelected,
        onSaveNewAddress = onSaveNewAddress,
        onCancelAddressSetting = onCancelAddressSetting
    )

    AddressEditDialog(
        uiState = uiState,
        onEditNameChanged = onEditNameChanged,
        onEditIconChanged = onEditIconChanged,
        onAddressSettingQueryChanged = onAddressSettingQueryChanged,
        onAddressSettingStopSelected = onAddressSettingStopSelected,
        onSaveEditedAddress = onSaveEditedAddress,
        onDeleteEditingAddress = onDeleteEditingAddress,
        onCancelEditingAddress = onCancelEditingAddress
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(contentPadding)
            .consumeWindowInsets(contentPadding)
            .padding(16.dp)
    ) {
        Text(
            text = "Plan Your Trip",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(24.dp))

        // Search Section with Swap Button
        Card(
            shape = RoundedCornerShape(16.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
        ) {
            Box(modifier = Modifier.padding(16.dp)) {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    AutocompleteField(
                        label = "From",
                        value = uiState.fromQuery,
                        onValueChange = onFromQueryChanged,
                        hints = uiState.fromHints,
                        onHintSelected = { onHintSelected(it, true) },
                        trailingIcon = {
                            IconButton(onClick = onCurrentLocationClick) {
                                Icon(Icons.Default.MyLocation, contentDescription = "Current Location")
                            }
                        }
                    )

                    AutocompleteField(
                        label = "To",
                        value = uiState.toQuery,
                        onValueChange = onToQueryChanged,
                        hints = uiState.toHints,
                        onHintSelected = { onHintSelected(it, false) }
                    )
                }

                Surface(
                    onClick = onSwapClick,
                    shape = RoundedCornerShape(50),
                    color = MaterialTheme.colorScheme.secondaryContainer,
                    modifier = Modifier
                        .align(Alignment.CenterEnd)
                        .padding(end = 48.dp, top = 8.dp)
                        .size(36.dp)
                ) {
                    Icon(
                        Icons.Default.SwapVert,
                        contentDescription = "Swap Locations",
                        modifier = Modifier.padding(8.dp),
                        tint = MaterialTheme.colorScheme.onSecondaryContainer
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Quick Actions (Home, Work, Custom + Add)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            val homeAddress = uiState.savedAddresses.find { it.type == "home" }
            SavedAddressButton(
                label = if (homeAddress == null) "Home" else null,
                icon = Icons.Default.Home,
                address = homeAddress,
                onClick = { onSavedAddressClick("home") },
                onLongClick = { onSavedAddressLongClick("home") }
            )

            val workAddress = uiState.savedAddresses.find { it.type == "work" }
            SavedAddressButton(
                label = if (workAddress == null) "Work" else null,
                icon = Icons.Default.Work,
                address = workAddress,
                onClick = { onSavedAddressClick("work") },
                onLongClick = { onSavedAddressLongClick("work") }
            )

            val extraAddresses = uiState.savedAddresses.filter { it.type != "home" && it.type != "work" }
            extraAddresses.forEach { address ->
                SavedAddressButton(
                    label = address.name,
                    icon = getIconForName(address.iconName),
                    address = address,
                    onClick = { onSavedAddressClick(address.type) },
                    onLongClick = { onSavedAddressLongClick(address.type) }
                )
            }

            FilledIconButton(
                onClick = onAddCustomAddressClick,
                modifier = Modifier.size(40.dp),
                colors = IconButtonDefaults.filledIconButtonColors(containerColor = MaterialTheme.colorScheme.secondaryContainer)
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add Address")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = onFindRoutesClick,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            enabled = !uiState.isLoading
        ) {
            if (uiState.isLoading) {
                CircularProgressIndicator(modifier = Modifier.size(24.dp), color = MaterialTheme.colorScheme.onPrimary)
            } else {
                Text("Search Routes", fontWeight = FontWeight.Bold)
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        if (uiState.error != null) {
            Text(text = uiState.error, color = MaterialTheme.colorScheme.error)
        }

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.weight(1f)
        ) {
            items(uiState.journeys) { journey ->
                RouteItem(
                    journey = journey,
                    onClick = {
                        selectedJourney = journey
                        showDetailSheet = true
                    }
                )
            }
        }
    }

    if (showDetailSheet && selectedJourney != null) {
        JourneyDetailPopup(
            journey = selectedJourney!!,
            sheetState = sheetState,
            onDismissRequest = { showDetailSheet = false }
        )
    }
}

/**
 * A smaller button representing a saved address.
 */
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SavedAddressButton(
    label: String?,
    icon: ImageVector,
    address: SavedAddress?,
    onClick: () -> Unit,
    onLongClick: () -> Unit
) {
    Surface(
        modifier = Modifier
            .height(40.dp)
            .combinedClickable(
                onClick = onClick,
                onLongClick = onLongClick
            ),
        shape = RoundedCornerShape(20.dp),
        border = BorderStroke(1.dp, if (address == null) Color.LightGray else MaterialTheme.colorScheme.outline),
        color = if (address == null) Color.Transparent else MaterialTheme.colorScheme.surface
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Icon(
                icon, 
                contentDescription = null, 
                modifier = Modifier.size(18.dp),
                tint = if (address == null) Color.Gray else MaterialTheme.colorScheme.primary
            )
            if (label != null) {
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = label, 
                    style = MaterialTheme.typography.labelMedium,
                    color = if (address == null) Color.Gray else MaterialTheme.colorScheme.onSurface
                )
            }
        }
    }
}

/**
 * An item representing a single journey option in the list.
 */
@Composable
fun RouteItem(
    journey: Journey,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "${journey.departureTime} - ${journey.arrivalTime}",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                
                Text(
                    text = journey.duration,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.secondary
                )
            }
            
            Spacer(modifier = Modifier.height(12.dp))
            
            // Visual Pathway
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                journey.legs.forEachIndexed { index, leg ->
                    if (leg.lineName != null) {
                        TransitLegSummary(lineName = leg.lineName, lineMode = leg.lineMode)
                    } else {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                Icons.AutoMirrored.Filled.DirectionsWalk,
                                contentDescription = null,
                                modifier = Modifier.size(16.dp),
                                tint = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Text(
                                text = "5", // Mock walking time
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                    if (index < journey.legs.size - 1) {
                        Icon(
                            Icons.AutoMirrored.Filled.KeyboardArrowRight,
                            contentDescription = null,
                            modifier = Modifier.size(14.dp),
                            tint = MaterialTheme.colorScheme.outline
                        )
                    }
                }
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun RoutingScreenPreview() {
    val mockJourneys = listOf(
        Journey(
            departureTime = "15:26",
            arrivalTime = "15:46",
            duration = "20 min",
            legs = listOf(
                Leg("Your location", "Eselsbergsteige", null, "Footpath", "15:26", "15:31"),
                Leg("Eselsbergsteige", "Ulm Rathaus", "5", "Stadtbus", "15:31", "15:41"),
                Leg("Ulm Rathaus", "Destination", null, "Footpath", "15:41", "15:46")
            )
        ),
        Journey(
            departureTime = "15:36",
            arrivalTime = "15:56",
            duration = "20 min",
            legs = listOf(
                Leg("Your location", "End", "1", "Straßenbahn", "15:36", "15:56")
            )
        )
    )

    val mockSavedAddresses = listOf(
        SavedAddress("home", "Home", "Main St 1", 52.0, 13.0),
        SavedAddress("work", "Work", "Office Blvd 2", 52.1, 13.1)
    )

    val mockUiState = RoutingUiState(
        fromQuery = "Alexanderplatz",
        toQuery = "Hauptbahnhof",
        journeys = mockJourneys,
        savedAddresses = mockSavedAddresses
    )

    SWU_plannerTheme {
        Surface(color = MaterialTheme.colorScheme.background) {
            RoutingContent(
                uiState = mockUiState,
                onFromQueryChanged = {},
                onToQueryChanged = {},
                onSwapClick = {},
                onHintSelected = { _, _ -> },
                onSavedAddressClick = {},
                onSavedAddressLongClick = {},
                onAddCustomAddressClick = {},
                onFindRoutesClick = {},
                onCurrentLocationClick = {},
                onAddressSettingQueryChanged = {},
                onAddressSettingStopSelected = {},
                onCancelAddressSetting = {},
                onEditNameChanged = {},
                onSaveEditedAddress = {},
                onCancelEditingAddress = {}
            )
        }
    }
}
