package com.example.swu_planner.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.swu_planner.data.model.Departure

// ui/departures/composables/DeparturesList.kt
@Composable
fun DeparturesList(departures: List<Departure>) {
    LazyColumn {
        items(departures.size) { index ->
            DepartureItem(departures[index])

            if (index < departures.size - 1) {
                Divider()
            }
        }
    }
}

@Composable
fun DepartureItem(departure: Departure) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                modifier = Modifier.weight(1f),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Route badge
                RouteBadge(routeName = departure.RouteName)

                Spacer(modifier = Modifier.width(12.dp))

                Column {
                    // Destination
                    Text(
                        text = departure.DepartureDirectionText,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(2.dp))

                    // Platform
                    Text(
                        text = "Platform ${departure.PlatformName ?: "-"}",
                        style = MaterialTheme.typography.labelSmall,
                        color = Color.DarkGray
                    )
                }
            }

            // Right side: Time and countdown
            Column(
                horizontalAlignment = Alignment.End
            ) {
                // Departure time
                Text(
                    text = formatTime(departure.DepartureTimeActual),
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(4.dp))

                // Countdown
                Text(
                    text = formatCountdown(departure.DepartureCountdown),
                    style = MaterialTheme.typography.bodySmall,
                    color = getCountdownColor(departure.DepartureDeviation)
                )

                // Deviation indicator
                if (departure.DepartureDeviation != 0) {
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "${if (departure.DepartureDeviation > 0) "+" else ""}${departure.DepartureDeviation}s",
                        style = MaterialTheme.typography.labelSmall,
                        color = Color.Red
                    )
                }
            }
        }
    }
}

fun formatTime(timestamp: String): String {
    return try {
        timestamp.substring(11, 16)  // "17:31"
    } catch (e: Exception) {
        ""
    }
}

fun formatCountdown(seconds: Int): String {
    return when {
        seconds < 60 -> "$seconds s"
        seconds < 3600 -> "${seconds / 60} min"
        else -> "${seconds / 3600} h"
    }
}

fun getCountdownColor(deviation: Int): Color {
    return when {
        deviation == 0 -> Color(0xFF4CAF50) // Material Green
        deviation < 300 -> Color(0xFFFBC02D) // Material Yellow/Gold
        else -> Color(0xFFF44336) // Material Red
    }
}

@Composable
fun RouteBadge(routeName: String) {
    val backgroundColor = getRouteColor(routeName)
    val shape = getRouteShape(routeName)

    Box(
        modifier = Modifier
            .size(26.dp)
            .background(color = backgroundColor, shape = shape),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = routeName,
            color = Color.White,
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            fontSize = if (routeName.length > 2) 15.sp else 18.sp
        )
    }
}

fun getRouteColor(routeName: String): Color {
    return when (routeName) {
        "1" -> Color(0xFFE31E24)
        "2" -> Color(0xFF3EAF7C)
        "4" -> Color(0xFF007D79)
        "5" -> Color(0xFF009EE3)
        "6" -> Color(0xFFF39200)
        "7" -> Color(0xFFAF007E)
        "8" -> Color(0xFF6B4095)
        "9" -> Color(0xFFD17FB1)
        "10" -> Color(0xFFA1A052)
        "11" -> Color(0xFF003882)
        "12" -> Color(0xFFFFD600)
        "13" -> Color(0xFF8C5B3E)
        "14" -> Color(0xFF00A4E4)
        "15" -> Color(0xFF9E7CB8)
        else -> Color.Gray
    }
}

fun getRouteShape(routeName: String): Shape {
    return when (routeName) {
        "1", "2" -> RoundedCornerShape(1.dp) // Tram lines (Squares)
        else -> CircleShape // Bus lines (Circles)
    }
}
