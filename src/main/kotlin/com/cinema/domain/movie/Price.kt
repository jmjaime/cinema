package com.cinema.domain.movie

import java.math.BigDecimal

data class Price(val amount:BigDecimal) {
    init {
        check(amount > BigDecimal.ZERO) { "price should be grater than 0" }
    }
}