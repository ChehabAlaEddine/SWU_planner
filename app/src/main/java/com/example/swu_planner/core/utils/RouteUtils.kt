package com.example.swu_planner.core.utils

import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.DirectionsWalk
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp

/**
 * Returns the characteristic [Color] for a given route.
 *
 * @param routeName The name of the route.
 * @return The [Color] assigned to the route, or [Color.Gray] if unknown.
 */
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

/**
 * Returns the characteristic [Shape] for a given route (e.g., Tram vs. Bus).
 *
 * @param routeName The name of the route.
 * @return The [Shape] assigned to the route.
 */
fun getRouteShape(routeName: String): Shape {
    return when (routeName) {
        "1", "2" -> RoundedCornerShape(1.dp) // Tram lines (Squares)
        else -> CircleShape // Bus lines (Circles)
    }
}

/**
 * Maps a string identifier to a corresponding [ImageVector] icon.
 */
fun getIconForName(name: String): ImageVector {
    return when (name) {
        "school" -> Icons.Default.School
        "star" -> Icons.Default.Star
        "favorite" -> Icons.Default.Favorite
        "gym" -> Icons.Default.FitnessCenter
        "work" -> Icons.Default.Work
        "home" -> Icons.Default.Home
        else -> Icons.Default.Place
    }
}

/**
 * Returns an appropriate icon for a transit mode.
 */
fun getTransportModeIcon(mode: String?): ImageVector {
    val m = mode?.lowercase() ?: ""
    return when {
        m.contains("bus") -> Icons.Default.DirectionsBus
        m.contains("tram") || m.contains("bahn") -> Icons.Default.Tram
        m.contains("subway") || m.contains("metro") || m.contains("u-bahn") -> Icons.Default.DirectionsSubway
        m.contains("walk") || m.contains("foot") -> Icons.AutoMirrored.Filled.DirectionsWalk
        else -> Icons.Default.DirectionsBus
    }
}
