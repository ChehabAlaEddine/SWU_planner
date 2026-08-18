package com.example.swu_planner.data.model.mapper

import com.example.swu_planner.data.model.PassageDto
import com.example.swu_planner.data.model.VehiclePassage

fun PassageDto.toDomain() = VehiclePassage(
    stopNumber = StopNumber,
    stopName = StopName,
    platformName = PlatformName,
    arrivalTimeScheduled = ArrivalTimeScheduled,
    arrivalTimeActual = ArrivalTimeActual,
    departureTimeScheduled = DepartureTimeScheduled,
    departureTimeActual = DepartureTimeActual,
    countdown = ArrivalCountdown ?: DepartureCountdown,
    deviation = ArrivalDeviation ?: DepartureDeviation
)
