package com.example.swu_planner.core.utils

import android.content.Context
import com.example.swu_planner.R
import com.google.android.gms.maps.model.BitmapDescriptor
import java.util.concurrent.ConcurrentHashMap

/**
 * Singleton manager to cache and provide [BitmapDescriptor] icons for the map.
 * This prevents redundant bitmap generation during recomposition.
 */
object MapIconManager {
    private val vehicleIconsCache = ConcurrentHashMap<String, BitmapDescriptor>()
    private var stopIconCache: BitmapDescriptor? = null

    /**
     * Provides the stop marker icon, generating it only once.
     */
    fun getStopIcon(context: Context): BitmapDescriptor? {
        if (stopIconCache == null) {
            stopIconCache = bitmapDescriptorFromVector(context, R.drawable.ic_stop_marker)
        }
        return stopIconCache
    }

    /**
     * Provides a vehicle icon for the given route, using a cache to avoid re-generation.
     */
    fun getVehicleIcon(context: Context, routeName: String): BitmapDescriptor {
        return vehicleIconsCache.getOrPut(routeName) {
            createVehicleIcon(context, routeName)
        }
    }

    /**
     * Clears the cache. Should be called if resources need to be freed.
     */
    fun clear() {
        vehicleIconsCache.clear()
        stopIconCache = null
    }
}
