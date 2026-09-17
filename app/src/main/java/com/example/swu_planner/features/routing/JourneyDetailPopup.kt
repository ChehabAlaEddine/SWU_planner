package com.example.swu_planner.features.routing

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.DirectionsWalk
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.Place
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.swu_planner.core.utils.getRouteColor
import com.example.swu_planner.core.utils.getTransportModeIcon
import com.example.swu_planner.data.model.Journey
import com.example.swu_planner.data.model.Leg

/**
 * A bottom sheet popup that displays the full details of a selected journey.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun JourneyDetailPopup(
    journey: Journey,
    sheetState: SheetState,
    onDismissRequest: () -> Unit
) {
    ModalBottomSheet(
        onDismissRequest = onDismissRequest,
        sheetState = sheetState,
        dragHandle = null,
        sheetGesturesEnabled = false,
        modifier = Modifier.fillMaxHeight(0.9f)
    ) {
        JourneyDetailContent(journey, onDismissRequest)
    }
}

/**
 * The content of the journey detail popup.
 */
@Composable
fun JourneyDetailContent(journey: Journey, onDismissRequest: () -> Unit = {}) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surface)
    ) {
        // Top Summary Bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                modifier = Modifier.weight(1f),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                journey.legs.forEachIndexed { index, leg ->
                    if (leg.lineName != null) {
                        TransitLegSummary(
                            lineName = leg.lineName,
                            lineMode = leg.lineMode,
                            badgeSize = 22.dp
                        )
                    } else {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.AutoMirrored.Filled.DirectionsWalk, null, modifier = Modifier.size(18.dp))
                            Text("11", fontSize = 10.sp, color = Color.Gray)
                        }
                    }
                    if (index < journey.legs.size - 1) {
                        Icon(Icons.AutoMirrored.Filled.KeyboardArrowRight, null, modifier = Modifier.size(14.dp), tint = Color.Gray)
                    }
                }
            }
            IconButton(onClick = onDismissRequest) {
                Icon(Icons.Default.Close, contentDescription = "Close")
            }
        }

        HorizontalDivider(thickness = 0.5.dp, color = Color.LightGray)

        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        ) {
            item {
                TimelineNode(
                    time = journey.departureTime,
                    title = "Your location",
                    icon = {
                        Box(
                            modifier = Modifier
                                .size(12.dp)
                                .background(Color(0xFF4285F4), CircleShape)
                                .padding(2.dp)
                                .background(Color.White, CircleShape)
                                .padding(2.dp)
                                .background(Color(0xFF4285F4), CircleShape)
                        )
                    },
                    lineBelow = LineType.DOTTED
                )
            }

            itemsIndexed(journey.legs) { _, leg ->
                if (leg.lineName == null) {
                    TimelineNode(
                        title = "Walk 11 min (700 m)",
                        isAction = true,
                        icon = { Icon(Icons.AutoMirrored.Filled.DirectionsWalk, null, modifier = Modifier.size(20.dp)) },
                        lineAbove = LineType.DOTTED,
                        lineBelow = LineType.DOTTED
                    )
                } else {
                    TransitLegTimeline(leg)
                }
            }

            item {
                TimelineNode(
                    time = journey.arrivalTime,
                    title = journey.legs.lastOrNull()?.destination ?: "Destination",
                    icon = { Icon(Icons.Default.Place, null, modifier = Modifier.size(24.dp), tint = Color(0xFFEA4335)) },
                    lineAbove = LineType.DOTTED
                )
            }
        }
    }
}

enum class LineType { NONE, SOLID, DOTTED }

@Composable
fun TimelineLine(type: LineType, color: Color, modifier: Modifier = Modifier) {
    Canvas(modifier = modifier.fillMaxHeight().width(4.dp)) {
        val pathEffect = if (type == LineType.DOTTED) PathEffect.dashPathEffect(floatArrayOf(5f, 5f), 0f) else null
        drawLine(
            color = color,
            start = Offset(size.width / 2, 0f),
            end = Offset(size.width / 2, size.height),
            strokeWidth = if (type == LineType.SOLID) 8.dp.toPx() else 4.dp.toPx(),
            pathEffect = pathEffect
        )
    }
}

@Composable
fun TimelineNode(
    time: String? = null,
    title: String,
    subtitle: String? = null,
    icon: @Composable () -> Unit,
    lineAbove: LineType = LineType.NONE,
    lineBelow: LineType = LineType.NONE,
    lineColor: Color = Color.LightGray,
    isAction: Boolean = false
) {
    Row(modifier = Modifier.fillMaxWidth().height(IntrinsicSize.Min)) {
        Column(
            modifier = Modifier.width(40.dp).fillMaxHeight(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.Center) {
                if (lineAbove != LineType.NONE) {
                    TimelineLine(lineAbove, lineColor, modifier = Modifier.fillMaxHeight())
                }
            }
            Box(modifier = Modifier.size(24.dp), contentAlignment = Alignment.Center) {
                icon()
            }
            Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.Center) {
                if (lineBelow != LineType.NONE) {
                    TimelineLine(lineBelow, lineColor, modifier = Modifier.fillMaxHeight())
                }
            }
        }

        Column(modifier = Modifier.weight(1f).padding(bottom = 24.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = title,
                    modifier = Modifier.weight(1f),
                    style = if (isAction) MaterialTheme.typography.bodyMedium else MaterialTheme.typography.titleMedium,
                    fontWeight = if (isAction) FontWeight.Normal else FontWeight.Bold
                )
                if (time != null) {
                    Text(time, style = MaterialTheme.typography.bodyMedium)
                }
                if (isAction) {
                    Icon(Icons.AutoMirrored.Filled.KeyboardArrowRight, null, modifier = Modifier.size(20.dp), tint = Color.Gray)
                }
            }
            if (subtitle != null) {
                Text(subtitle, style = MaterialTheme.typography.bodySmall, color = Color.Gray)
            }
        }
    }
}

@Composable
fun TransitLegTimeline(leg: Leg) {
    val modeColor = leg.lineName?.let { getRouteColor(it) } ?: Color.Gray
    
    Column(modifier = Modifier.fillMaxWidth()) {
        TimelineNode(
            title = leg.origin,
            icon = {
                Surface(
                    shape = CircleShape,
                    color = Color.White,
                    border = BorderStroke(2.dp, modeColor),
                    modifier = Modifier.size(12.dp)
                ) {}
            },
            lineAbove = LineType.DOTTED,
            lineBelow = LineType.SOLID,
            lineColor = modeColor
        )

        Row(modifier = Modifier.fillMaxWidth().height(IntrinsicSize.Min)) {
            Box(modifier = Modifier.width(40.dp).fillMaxHeight(), contentAlignment = Alignment.Center) {
                TimelineLine(LineType.SOLID, modeColor, modifier = Modifier.fillMaxHeight())
            }
            
            Column(modifier = Modifier.padding(bottom = 24.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    leg.lineName?.let {
                        TransitBadge(routeName = it, size = 24.dp, textStyle = MaterialTheme.typography.labelSmall)
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(leg.destination, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.weight(1f))
                    Text(leg.departureTime, fontWeight = FontWeight.Bold)
                }
                Text("Scheduled", style = MaterialTheme.typography.labelSmall, color = Color.Gray)
                
                Spacer(modifier = Modifier.height(12.dp))
                
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = Color.LightGray.copy(alpha = 0.1f),
                    border = BorderStroke(0.5.dp, Color.LightGray.copy(alpha = 0.5f))
                ) {
                    Row(modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp), verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Group, null, modifier = Modifier.size(16.dp), tint = Color.Gray)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Not too crowded", style = MaterialTheme.typography.labelSmall, color = Color.Gray)
                        Icon(Icons.Default.ExpandMore, null, modifier = Modifier.size(16.dp), tint = Color.Gray)
                    }
                }
                
                Spacer(modifier = Modifier.height(16.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.ExpandMore, null, modifier = Modifier.size(20.dp), tint = Color.Gray)
                    Text("Ride 7 stops (9 min)", style = MaterialTheme.typography.bodySmall, color = Color.Gray)
                }
            }
        }

        TimelineNode(
            time = leg.arrivalTime,
            title = leg.destination,
            icon = {
                Surface(
                    shape = CircleShape,
                    color = Color.White,
                    border = BorderStroke(2.dp, modeColor),
                    modifier = Modifier.size(12.dp)
                ) {}
            },
            lineAbove = LineType.SOLID,
            lineBelow = LineType.DOTTED,
            lineColor = modeColor
        )
    }
}

@Preview(showBackground = true)
@Composable
fun JourneyDetailPreview() {
    val mockJourney = Journey(
        departureTime = "10:30",
        arrivalTime = "11:15",
        duration = "45m",
        legs = listOf(
            Leg(
                origin = "Alexanderplatz",
                destination = "Hauptbahnhof",
                lineName = "5",
                lineMode = "Stadtbus",
                departureTime = "10:30",
                arrivalTime = "10:45"
            ),
            Leg(
                origin = "Hauptbahnhof",
                destination = "Brandenburger Tor",
                lineName = null,
                lineMode = "walk",
                departureTime = "10:45",
                arrivalTime = "11:15"
            )
        )
    )

    MaterialTheme {
        Surface(color = MaterialTheme.colorScheme.background) {
            JourneyDetailContent(journey = mockJourney, onDismissRequest = {})
        }
    }
}
