package com.example.swu_planner.composables

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.swu_planner.data.model.Stop
import com.example.swu_planner.ui.departures.DeparturesUiState
import com.example.swu_planner.ui.departures.DeparturesViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DeparturePopup(
    selectedStop: Stop,
    departuresViewModel: DeparturesViewModel,
    sheetState: SheetState,
    onDismissRequest: () -> Unit
) {
    val departuresState by departuresViewModel.uiState.collectAsState()

    ModalBottomSheet(
        onDismissRequest = onDismissRequest,
        sheetState = sheetState,
        modifier = Modifier.fillMaxHeight(0.6f)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = selectedStop.name,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "Stop #${selectedStop.number}",
                style = MaterialTheme.typography.bodyMedium
            )

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
                    DeparturesList(state.departures)
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
