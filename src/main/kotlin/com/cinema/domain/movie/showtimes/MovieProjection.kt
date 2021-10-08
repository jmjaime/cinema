package com.cinema.domain.movie.showtimes

import java.math.BigDecimal
import java.time.LocalDateTime

data class MovieProjection(val startAt: LocalDateTime, val price: BigDecimal) {
}