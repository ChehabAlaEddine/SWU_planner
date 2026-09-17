package com.example.swu_planner.core.utils

import androidx.compose.ui.graphics.Color

/**
 * Formats a timestamp string into a "HH:mm" time string.
 *
 * @param timestamp The timestamp in ISO format or similar.
 * @return The formatted time string, or an empty string on error.
 */
fun formatTime(timestamp: String): String {
    return try {
        timestamp.substring(11, 16)  // "17:31"
    } catch (e: Exception) {
        ""
    }
}

/**
 * Formats a duration in seconds into a human-readable countdown string.
 *
 * @param seconds The number of seconds.
 * @return A string like "30 s", "5 min", or "1 h".
 */
fun formatCountdown(seconds: Int): String {
    return when {
        seconds < 60 -> "$seconds s"
        seconds < 3600 -> "${seconds / 60} min"
        else -> "${seconds / 3600} h"
    }
}

/**
 * Returns a [Color] based on the vehicle's punctuality (deviation in seconds).
 *
 * @param deviation The deviation from the schedule in seconds.
 * @return A [Color] indicating status: green for on time, yellow for slight delay, red for late.
 */
fun getCountdownColor(deviation: Int): Color {
    return when {
        kotlin.math.abs(deviation) <= 60 -> Color(0xFF4CAF50) // Material Green (On Time)
        deviation < 300 -> Color(0xFFFBC02D) // Material Yellow/Gold (Slightly Late)
        else -> Color(0xFFF44336) // Material Red (Late)
    }
}

/**
 * Returns a [Color] indicating if a vehicle is on time based on its deviation.
 *
 * @param deviation The deviation from the schedule in seconds, or null.
 * @return A [Color] (Green for on-time/early, Red for late, or Unspecified).
 */
fun getOnTimeColor(deviation: Int?): Color {
    return when {
        deviation == null -> Color.Unspecified
        kotlin.math.abs(deviation) <= 60 -> Color(0xFF4CAF50) // Green (On Time)
        deviation > 60 -> Color(0xFFF44336) // Red (Late)
        else -> Color(0xFF4CAF50) // Green (Early)
    }
}
