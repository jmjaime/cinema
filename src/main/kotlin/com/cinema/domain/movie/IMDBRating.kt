package com.cinema.domain.movie

import java.math.BigDecimal
import java.math.BigDecimal.TEN
import java.math.BigDecimal.ZERO

data class IMDBRating(val rating: BigDecimal, val votes: Long) {
    init {
        check(votes >= 0) { "votes should be positive" }
        check(rating <= TEN && rating >= ZERO) { "Rating should be between 0 and 10" }
    }
}
