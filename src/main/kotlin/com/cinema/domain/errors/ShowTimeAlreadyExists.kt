package com.cinema.domain.errors

import java.time.DayOfWeek
import java.time.LocalTime

class ShowTimeAlreadyExists(movieId: String, dayOfWeek: DayOfWeek, startAt: LocalTime) :
    RuntimeException("Already exists showtime for movie $movieId at $dayOfWeek $startAt")
