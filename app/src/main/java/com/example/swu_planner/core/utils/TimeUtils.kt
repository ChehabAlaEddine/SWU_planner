package com.example.swu_planner.core.utils

import androidx.compose.ui.graphics.Color

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
        kotlin.math.abs(deviation) <= 60 -> Color(0xFF4CAF50) // Material Green (On Time)
        deviation < 300 -> Color(0xFFFBC02D) // Material Yellow/Gold (Slightly Late)
        else -> Color(0xFFF44336) // Material Red (Late)
    }
}

fun getOnTimeColor(deviation: Int?): Color {
    return when {
        deviation == null -> Color.Unspecified
        kotlin.math.abs(deviation) <= 60 -> Color(0xFF4CAF50) // Green (On Time)
        deviation > 60 -> Color(0xFFF44336) // Red (Late)
        else -> Color(0xFF4CAF50) // Green (Early)
    }
}
