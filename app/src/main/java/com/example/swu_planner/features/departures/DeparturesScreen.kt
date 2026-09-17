package com.example.swu_planner.features.departures

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.swu_planner.features.common.EmptyState
import com.example.swu_planner.features.common.ErrorState

/**
 * Screen that allows users to search for departures by stop number.
 *
 * @param viewModel The ViewModel providing departure data.
 * @param contentPadding Padding values from the Scaffold to handle system bars.
 */
@Composable
fun DeparturesScreen(
    viewModel: DeparturesViewModel,
    contentPadding: PaddingValues = PaddingValues(0.dp)
) {
    val uiState by viewModel.uiState.collectAsState()
    var stopNumber by remember { mutableStateOf("1010") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(contentPadding)
            .consumeWindowInsets(contentPadding)
            .padding(16.dp)
    ) {
        SearchBar(
            stopNumber = stopNumber,
            onStopNumberChange = { stopNumber = it },
            onSearch = { viewModel.loadDepartures(stopNumber) }
        )

        Spacer(modifier = Modifier.height(16.dp))

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

/**
 * A search bar for inputting stop numbers.
 */
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

/**
 * Composable shown when the screen is in an idle state.
 */
@Composable
fun IdleState() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text("Enter a stop number to get departures")
    }
}
