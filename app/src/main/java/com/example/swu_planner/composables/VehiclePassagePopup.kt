package com.example.swu_planner.composables

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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.swu_planner.R
import com.example.swu_planner.data.model.Trip
import com.example.swu_planner.data.model.VehiclePassage
import com.example.swu_planner.ui.vehicles.VehiclePassageUiState
import com.example.swu_planner.ui.vehicles.VehicleViewModel
import com.example.swu_planner.utils.formatTime
import com.example.swu_planner.utils.getOnTimeColor

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VehiclePassagePopup(
    trip: Trip,
    viewModel: VehicleViewModel,
    sheetState: SheetState,
    onDismissRequest: () -> Unit
) {
    val passageState by viewModel.passageUiState.collectAsState()

    ModalBottomSheet(
        onDismissRequest = onDismissRequest,
        sheetGesturesEnabled = false,
        sheetState = sheetState,
        modifier = Modifier.fillMaxHeight(0.7f)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Header
            Row(verticalAlignment = Alignment.CenterVertically) {
                RouteBadge(routeName = trip.routeNumber?.rem(100).toString() ?: "")
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = trip.destination ?: "Unknown Direction",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )
            }
            Text(
                text = "Vehicle #${trip.vehicleNumber}",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.secondary
            )

            Spacer(modifier = Modifier.height(16.dp))

            when (val state = passageState) {
                is VehiclePassageUiState.Loading -> {
                    Box(Modifier.fillMaxWidth().height(200.dp), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                }
                is VehiclePassageUiState.Success -> {
                    VehiclePassageList(state.passages)
                }
                is VehiclePassageUiState.Error -> {
                    ErrorState(state.message)
                }
                else -> {}
            }
        }
    }
}

@Composable
fun VehiclePassageList(passages: List<VehiclePassage>) {
    LazyColumn {
        itemsIndexed(passages) { index, passage ->
            VehiclePassageItem(passage)
        }
    }
}

@Composable
fun VehiclePassageItem(passage: VehiclePassage) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = painterResource(id = R.drawable.ic_stop_marker),
            contentDescription = null,
            tint = Color.Unspecified,
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.width(12.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = passage.stopName,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.SemiBold
            )
        }
        val isDelayedOrEarly = passage.deviation != null && kotlin.math.abs(passage.deviation) > 120
        val actualTime = passage.arrivalTimeActual ?: passage.departureTimeActual ?: ""
        val scheduledTime = passage.arrivalTimeScheduled ?: passage.departureTimeScheduled ?: ""

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.End,
            modifier = Modifier.width(100.dp) // Fixed width for the time section
        ) {
            if (isDelayedOrEarly && actualTime.isNotEmpty()) {
                val timeColor = getOnTimeColor(passage.deviation)
                Text(
                    text = formatTime(actualTime),
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold,
                    color = timeColor,
                    modifier = Modifier.width(42.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
            } else {
                // Spacer to keep scheduled time aligned when no actual time is shown
                Spacer(modifier = Modifier.width(46.dp))
            }

            Text(
                text = formatTime(scheduledTime),
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Normal,
                color = Color.Black
            )
        }
    }
}
