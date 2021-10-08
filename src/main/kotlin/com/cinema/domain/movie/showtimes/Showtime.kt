package com.cinema.domain.movie.showtimes

import com.cinema.domain.movie.Price
import java.time.DayOfWeek
import java.time.LocalTime

data class Showtime(val dayOfWeek: DayOfWeek, val startAt: LocalTime, val price: Price)
