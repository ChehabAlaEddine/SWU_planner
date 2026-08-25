package com.example.swu_planner.features.departures

import android.util.Log
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.swu_planner.R
import com.example.swu_planner.data.model.Stop
import com.example.swu_planner.features.common.EmptyState
import com.example.swu_planner.features.common.ErrorState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DeparturePopup(
    selectedStop: Stop,
    departuresViewModel: DeparturesViewModel,
    sheetState: SheetState,
    onDismissRequest: () -> Unit
) {
    val departuresState by departuresViewModel.uiState.collectAsState()

    val platforms = remember(selectedStop) {
        selectedStop.platforms.map { it.platformName }.distinct().sorted()
    }
    var selectedPlatforms by remember(selectedStop) { 
        mutableStateOf(platforms.toSet()) 
    }

    ModalBottomSheet(
        onDismissRequest = onDismissRequest,
        sheetGesturesEnabled = false,
        sheetState = sheetState,
        modifier = Modifier.fillMaxHeight(0.6f)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_stop_marker),
                    contentDescription = null,
                    tint = Color.Unspecified,
                    modifier = Modifier.size(32.dp)
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = selectedStop.name,
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(4.dp))

            val routes = remember(selectedStop) {
                selectedStop.platforms
                    .flatMap { it.routes }
                    .map { it.number.rem(100).toString() }
                    .distinct()
                    .sortedBy { it.toIntOrNull() ?: Int.MAX_VALUE }
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "Lines: ",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.SemiBold
                )
                Row(
                    modifier = Modifier.horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    routes.forEach { routeName ->
                        RouteBadge(routeName = routeName)
                        Log.d("DeparturePopup", "Showing route: $routeName")
                    }
                }
            }

            Text(
                text = "Stop #${selectedStop.number}",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.secondary
            )

            if (platforms.size > 1) {
                Spacer(modifier = Modifier.height(12.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    platforms.forEach { platform ->
                        val isSelected = selectedPlatforms.contains(platform)
                        val togglePlatform = {
                            selectedPlatforms = if (isSelected) {
                                selectedPlatforms - platform
                            } else {
                                selectedPlatforms + platform
                            }
                        }

                        if (isSelected) {
                            Button(
                                onClick = togglePlatform,
                                modifier = Modifier.weight(1f)
                            ) {
                                Text("Platform $platform")
                            }
                        } else {
                            OutlinedButton(
                                onClick = togglePlatform,
                                modifier = Modifier.weight(1f)
                            ) {
                                Text("Platform $platform")
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            when (val state = departuresState) {
                is DeparturesUiState.Loading -> {
                    Box(
                        Modifier
                            .fillMaxWidth()
                            .height(200.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }
                is DeparturesUiState.Success -> {
                    val filteredDepartures = state.departures.filter { 
                        selectedPlatforms.contains(it.PlatformName) 
                    }
                    DeparturesList(filteredDepartures)
                }
                is DeparturesUiState.Empty -> {
                    EmptyState(state.message)
                }
                is DeparturesUiState.Error -> {
                    ErrorState(state.message)
                }
                else -> {}
            }
        }
    }
}
