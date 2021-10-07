package com.cinema.domain.movie.showtimes

import java.math.BigDecimal
import java.time.DayOfWeek
import java.time.LocalTime

data class Showtime(val dayOfWeek: DayOfWeek, val startAt: LocalTime, val price: BigDecimal) {
    init {
        check(price > BigDecimal.ZERO) { "price should be grater than 0" }
    }
}
