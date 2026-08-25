package com.example.swu_planner.features.stops

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.swu_planner.core.theme.SWU_plannerTheme
import com.example.swu_planner.data.model.Stop
import com.example.swu_planner.features.departures.RouteBadge

@Composable
fun StopsScreen(viewModel: StopsViewModel) {
    val uiState by viewModel.uiState.collectAsState()

    StopsScreenContent(
        uiState = uiState,
        onLoadAllStops = { viewModel.loadAllStops() },
        onLoadStop = { viewModel.loadStop(it) },
        onBack = { viewModel.loadAllStops() }
    )
}

@Composable
fun StopsScreenContent(
    uiState: StopsUiState,
    onLoadAllStops: () -> Unit,
    onLoadStop: (String) -> Unit,
    onBack: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Spacer(modifier = Modifier.height(50.dp))

        Button(
            onClick = onLoadAllStops,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Get All Stops")
        }

        Spacer(modifier = Modifier.height(16.dp))

        when (val state = uiState) {
            is StopsUiState.Idle -> {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("Press the button to load stops")
                }
            }
            is StopsUiState.Loading -> {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }
            is StopsUiState.Success -> {
                LazyColumn {
                    items(state.stops) { stop ->
                        Text(
                            text = "${stop.number}: ${stop.name}",
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { onLoadStop(stop.number.toString()) }
                                .padding(16.dp)
                        )
                        HorizontalDivider()
                    }
                }
            }
            is StopsUiState.StopDetail -> {
                StopDetailContent(
                    stop = state.stop,
                    onBack = onBack
                )
            }
            is StopsUiState.Empty -> {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(state.message)
                }
            }
            is StopsUiState.Error -> {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(text = state.message, color = MaterialTheme.colorScheme.error)
                }
            }
        }
    }
}

@Composable
fun StopDetailContent(stop: Stop, onBack: () -> Unit) {
    Column(modifier = Modifier.fillMaxSize()) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = onBack) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back"
                )
            }
            Text(
                text = "Stop Details",
                style = MaterialTheme.typography.headlineMedium
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = stop.name,
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = "Stop Number: ${stop.number}",
            style = MaterialTheme.typography.bodyLarge
        )
        Text(
            text = "Location: ${stop.latitude}, ${stop.longitude}",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.secondary
        )

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Platforms",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )

        LazyColumn {
            items(stop.platforms) { platform ->
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 12.dp)
                ) {
                    Text(
                        text = "Platform ${platform.platformName} (${platform.code})",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold
                    )
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    // Display routes using Row and badges
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        platform.routes.forEach { route ->
                            RouteBadge(routeName = route.name)
                        }
                    }
                }
                HorizontalDivider()
            }
        }
    }
}

@androidx.compose.ui.tooling.preview.Preview(showBackground = true)
@Composable
fun StopsScreenPreview() {
    SWU_plannerTheme {
        StopsScreenContent(
            uiState = StopsUiState.Success(
                stops = listOf(
                    Stop(1, "Sample Stop 1", 0.0, 0.0, emptyList()),
                    Stop(2, "Sample Stop 2", 0.0, 0.0, emptyList())
                )
            ),
            onLoadAllStops = {},
            onLoadStop = {},
            onBack = {}
        )
    }
}
