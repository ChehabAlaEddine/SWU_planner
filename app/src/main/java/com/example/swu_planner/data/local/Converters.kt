package com.example.swu_planner.data.local

import androidx.room.TypeConverter
import com.example.swu_planner.data.dto.CoordinatesDto
import com.example.swu_planner.data.dto.StopPointDto
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

/**
 * Room TypeConverters for converting complex data types to/from JSON strings.
 */
class Converters {
    private val gson = Gson()

    /**
     * Converts a [CoordinatesDto] to a JSON string.
     */
    @TypeConverter
    fun fromCoordinates(value: CoordinatesDto): String = gson.toJson(value)

    /**
     * Converts a JSON string back to a [CoordinatesDto].
     */
    @TypeConverter
    fun toCoordinates(value: String): CoordinatesDto = gson.fromJson(value, CoordinatesDto::class.java)

    /**
     * Converts a list of [StopPointDto] to a JSON string.
     */
    @TypeConverter
    fun fromStopPointsList(value: List<StopPointDto>): String = gson.toJson(value)

    /**
     * Converts a JSON string back to a list of [StopPointDto].
     */
    @TypeConverter
    fun toStopPointsList(value: String): List<StopPointDto> {
        val listType = object : TypeToken<List<StopPointDto>>() {}.type
        return gson.fromJson(value, listType)
    }
}
