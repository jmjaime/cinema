package com.cinema.domain.movie.showtimes

import com.cinema.domain.movie.Price
import java.time.LocalTime

data class Showtime(val startAt: LocalTime, val price: Price)
