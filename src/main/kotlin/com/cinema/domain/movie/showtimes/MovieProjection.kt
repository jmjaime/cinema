package com.cinema.domain.movie.showtimes

import com.cinema.domain.movie.Price
import java.time.LocalDateTime

data class MovieProjection(val startAt: LocalDateTime, val price: Price) {
}