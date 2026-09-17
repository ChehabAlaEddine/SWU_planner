package com.example.swu_planner.features.departures

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.swu_planner.core.utils.formatTime
import com.example.swu_planner.core.utils.getRouteColor
import com.example.swu_planner.core.utils.getRouteShape
import com.example.swu_planner.data.model.Departure
import kotlin.math.abs

/**
 * A scrollable list of departures.
 */
@Composable
fun DeparturesList(departures: List<Departure>) {
    Log.d("DeparturesList", "Departures: $departures")
    LazyColumn {
        items(departures.size) { index ->
            DepartureItem(departures[index])
        }
    }
}

/**
 * An item representing a single departure in the list.
 */
@Composable
fun DepartureItem(departure: Departure) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Left part: Route badge and Destination
            Row(
                modifier = Modifier.weight(1f),
                verticalAlignment = Alignment.CenterVertically
            ) {
                RouteBadge(routeName = departure.RouteName)

                Spacer(modifier = Modifier.width(12.dp))

                Text(
                    text = departure.DepartureDirectionText,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1
                )
            }

            // Right part: Time, Deviation, and Platform side-by-side
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(start = 8.dp)
            ) {
                // Time (Fixed width for vertical alignment)
                Text(
                    text = formatTime(departure.DepartureTimeActual),
                    style = MaterialTheme.typography.bodyMedium,
                    fontSize = 14.sp,
                    modifier = Modifier.width(42.dp)
                )

                // Deviation (Fixed width to keep Platform aligned)
                Box(
                    modifier = Modifier.width(50.dp),
                    contentAlignment = Alignment.CenterStart
                ) {
                    if (abs(departure.DepartureDeviation) > 60) {
                        val minutes = abs(departure.DepartureDeviation) / 60
                        val (text, color) = if (departure.DepartureDeviation > 0) {
                            "+$minutes min" to Color.Red
                        } else {
                            "-$minutes min" to Color(0xFF4CAF50)
                        }
                        Text(
                            text = text,
                            style = MaterialTheme.typography.labelSmall,
                            color = color,
                            modifier = Modifier.padding(start = 4.dp),
                            maxLines = 1
                        )
                    }
                }

                // Platform (Fixed width)
                Text(
                    text = departure.PlatformName ?: "-",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.width(24.dp),
                    textAlign = TextAlign.End
                )
            }
        }
    }


/**
 * A colored badge showing the route name.
 */
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
