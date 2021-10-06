package com.cinema.domain.movie.showtimes

import java.math.BigDecimal
import java.time.LocalDateTime

data class Showtime(val id: String, val startAt: LocalDateTime, val price: BigDecimal) {
    init {
        check(id.isNotBlank()) { "id is mandatory" }
        check(price > BigDecimal.ZERO) { "price should be grater than 0" }
    }
}
