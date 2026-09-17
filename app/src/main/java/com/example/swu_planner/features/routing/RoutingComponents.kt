package com.example.swu_planner.features.routing

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.swu_planner.core.utils.getRouteColor
import com.example.swu_planner.core.utils.getRouteShape
import com.example.swu_planner.core.utils.getTransportModeIcon
import com.example.swu_planner.data.model.Stop

/**
 * A badge displaying the route number with its characteristic color and shape.
 */
@Composable
fun TransitBadge(
    routeName: String,
    modifier: Modifier = Modifier,
    size: Dp = 24.dp,
    textStyle: TextStyle = MaterialTheme.typography.labelMedium
) {
    Surface(
        color = getRouteColor(routeName),
        contentColor = Color.White,
        shape = getRouteShape(routeName),
        modifier = modifier
            .size(size)
            .aspectRatio(1f)
    ) {
        Box(contentAlignment = Alignment.Center) {
            Text(
                text = routeName,
                style = textStyle,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
        }
    }
}

/**
 * A component that displays a transport mode icon followed by a transit badge.
 */
@Composable
fun TransitLegSummary(
    lineName: String,
    lineMode: String?,
    modifier: Modifier = Modifier,
    badgeSize: Dp = 24.dp,
    iconSize: Dp = 18.dp
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Icon(
            imageVector = getTransportModeIcon(lineMode),
            contentDescription = null,
            modifier = Modifier.size(iconSize),
            tint = Color.Gray
        )
        TransitBadge(
            routeName = lineName,
            size = badgeSize
        )
    }
}

/**
 * A text field with autocomplete suggestions for stops.
 */
@Composable
fun AutocompleteField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    hints: List<Stop>,
    onHintSelected: (Stop) -> Unit,
    trailingIcon: @Composable (() -> Unit)? = null
) {
    Column {
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            label = { Text(label) },
            modifier = Modifier.fillMaxWidth(),
            trailingIcon = trailingIcon
        )
        if (hints.isNotEmpty()) {
            Card(
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                hints.forEach { stop ->
                    Text(
                        text = stop.name,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onHintSelected(stop) }
                            .padding(12.dp)
                    )
                }
            }
        }
    }
}
