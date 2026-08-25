package com.example.swu_planner.core.utils

import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp

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
