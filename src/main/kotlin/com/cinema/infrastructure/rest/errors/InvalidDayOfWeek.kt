package com.cinema.infrastructure.rest.errors

class InvalidDayOfWeek(dayOfWeek: String) : RuntimeException("Invalid day of week $dayOfWeek")
