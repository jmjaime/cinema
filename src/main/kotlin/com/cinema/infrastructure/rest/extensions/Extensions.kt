package com.cinema.infrastructure.rest.extensions

import com.cinema.infrastructure.rest.errors.InvalidDayOfWeek
import java.time.DayOfWeek


fun String.toDayOfWeek() = uppercase().let { dayUpperCase ->
    DayOfWeek.values().firstOrNull { it.name == dayUpperCase } ?: throw InvalidDayOfWeek(dayUpperCase)
}