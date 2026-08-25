package com.example.swu_planner.data.local

import androidx.room.TypeConverter
import com.example.swu_planner.data.dto.CoordinatesDto
import com.example.swu_planner.data.dto.StopPointDto
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class Converters {
    private val gson = Gson()

    @TypeConverter
    fun fromCoordinates(value: CoordinatesDto): String = gson.toJson(value)

    @TypeConverter
    fun toCoordinates(value: String): CoordinatesDto = gson.fromJson(value, CoordinatesDto::class.java)

    @TypeConverter
    fun fromStopPointsList(value: List<StopPointDto>): String = gson.toJson(value)

    @TypeConverter
    fun toStopPointsList(value: String): List<StopPointDto> {
        val listType = object : TypeToken<List<StopPointDto>>() {}.type
        return gson.fromJson(value, listType)
    }
}
