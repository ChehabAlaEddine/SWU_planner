package com.example.swu_planner.core.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.RectF
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.ui.graphics.toArgb
import androidx.core.content.ContextCompat
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory

/**
 * Converts a vector resource into a [BitmapDescriptor] for use as a map marker.
 *
 * @param context The application context.
 * @param vectorResId The resource ID of the vector drawable.
 * @return A [BitmapDescriptor] if successful, or null otherwise.
 */
fun bitmapDescriptorFromVector(context: Context, vectorResId: Int): BitmapDescriptor? {
    val vectorDrawable = ContextCompat.getDrawable(context, vectorResId) ?: return null
    vectorDrawable.setBounds(0, 0, vectorDrawable.intrinsicWidth, vectorDrawable.intrinsicHeight)
    val bitmap = Bitmap.createBitmap(
        vectorDrawable.intrinsicWidth,
        vectorDrawable.intrinsicHeight,
        Bitmap.Config.ARGB_8888
    )
    val canvas = Canvas(bitmap)
    vectorDrawable.draw(canvas)
    return BitmapDescriptorFactory.fromBitmap(bitmap)
}

/**
 * Creates a custom [BitmapDescriptor] for a vehicle icon based on the route name.
 *
 * The icon includes a background shape (circle for bus, rounded square for tram)
 * colored according to the route, with the route name text centered inside.
 *
 * @param context The application context.
 * @param routeName The name of the route (e.g., "1", "6").
 * @return A [BitmapDescriptor] representing the vehicle icon.
 */
fun createVehicleIcon(context: Context, routeName: String): BitmapDescriptor {
    val size = (30 * context.resources.displayMetrics.density).toInt()
    val bitmap = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888)
    val canvas = Canvas(bitmap)

    val color = getRouteColor(routeName).toArgb()
    val paint = Paint().apply {
        this.color = color
        isAntiAlias = true
        style = Paint.Style.FILL
    }

    val shape = getRouteShape(routeName)
    if (shape == CircleShape) {
        canvas.drawCircle(size / 2f, size / 2f, size / 2f, paint)
    } else {
        // Assume RoundedCornerShape or similar for tram
        val rect = RectF(0f, 0f, size.toFloat(), size.toFloat())
        val cornerRadius = 2 * context.resources.displayMetrics.density
        canvas.drawRoundRect(rect, cornerRadius, cornerRadius, paint)
    }

    // Draw text
    val textPaint = Paint().apply {
        this.color = android.graphics.Color.WHITE
        isAntiAlias = true
        textAlign = Paint.Align.CENTER
        textSize = (16 * context.resources.displayMetrics.density)
        typeface = android.graphics.Typeface.DEFAULT_BOLD
    }

    val textBounds = Rect()
    textPaint.getTextBounds(routeName, 0, routeName.length, textBounds)
    val textY = (size / 2f) - textBounds.centerY()
    canvas.drawText(routeName, size / 2f, textY, textPaint)

    return BitmapDescriptorFactory.fromBitmap(bitmap)
}
