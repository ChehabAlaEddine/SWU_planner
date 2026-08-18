package com.example.swu_planner.composables

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.swu_planner.ui.departures.DeparturesUiState
import com.example.swu_planner.ui.departures.DeparturesViewModel

// ui/departures/DeparturesScreen.kt
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.Info
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color

@Composable
fun DeparturesScreen(
    viewModel: DeparturesViewModel  // ← Receive ViewModel as parameter (no hiltViewModel)
) {
    val uiState by viewModel.uiState.collectAsState()

    var stopNumber by remember { mutableStateOf("1010") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Spacer(modifier = Modifier.height(50.dp))
        // Search bar
        SearchBar(
            stopNumber = stopNumber,
            onStopNumberChange = { stopNumber = it },
            onSearch = { viewModel.loadDepartures(stopNumber) }
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Handle different UI states
        when (val state = uiState) {
            is DeparturesUiState.Idle -> {
                IdleState()
            }

            is DeparturesUiState.Loading -> {
                //LoadingIndicator()
            }

            is DeparturesUiState.Success -> {
                DeparturesList(state.departures)
            }

            is DeparturesUiState.Empty -> {
                EmptyState(state.message)
            }

            is DeparturesUiState.Error -> {
                ErrorState(state.message)
            }
        }
    }
}

@Composable
fun SearchBar(
    stopNumber: String,
    onStopNumberChange: (String) -> Unit,
    onSearch: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        TextField(
            value = stopNumber,
            onValueChange = onStopNumberChange,
            label = { Text("Stop Number") },
            modifier = Modifier.weight(1f),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )

        Spacer(modifier = Modifier.width(8.dp))

        Button(onClick = onSearch) {
            Text("Search")
        }
    }
}

@Composable
fun IdleState() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text("Enter a stop number to get departures")
    }
}

@Composable
fun ErrorState(message: String) {
    Log.e("DeparturesScreen", "Error: $message")
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFFEBEE)),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(
                imageVector = Icons.Filled.Error,
                contentDescription = "Error",
                tint = Color.Red,
                modifier = Modifier.size(64.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = message,
                color = Color.Red,
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}

@Composable
fun EmptyState(message: String) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Spacer(modifier = Modifier.height(30.dp))
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(
                imageVector = Icons.Default.Info,
                contentDescription = "Empty",
                tint = Color.Gray,
                modifier = Modifier.size(64.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = message,
                color = Color.Gray,
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}