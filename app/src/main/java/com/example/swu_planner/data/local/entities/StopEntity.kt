package com.example.swu_planner.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.swu_planner.data.dto.CoordinatesDto
import com.example.swu_planner.data.dto.StopDto
import com.example.swu_planner.data.dto.StopPointDto

@Entity(tableName = "stops")
data class StopEntity(
    @PrimaryKey
    val stopNumber: Int,
    val stopCode: String,
    val stopName: String,
    val stopAnnouncement: String?,
    val stopCoordinates: CoordinatesDto,
    val stopPoints: List<StopPointDto>
)

fun StopDto.toEntity() = StopEntity(
    stopNumber = StopNumber,
    stopCode = StopCode,
    stopName = StopName,
    stopAnnouncement = StopAnnouncement,
    stopCoordinates = StopCoordinates,
    stopPoints = StopPoints
)

fun StopEntity.toDto() = StopDto(
    StopNumber = stopNumber,
    StopCode = stopCode,
    StopName = stopName,
    StopAnnouncement = stopAnnouncement,
    StopCoordinates = stopCoordinates,
    StopPoints = stopPoints
)
