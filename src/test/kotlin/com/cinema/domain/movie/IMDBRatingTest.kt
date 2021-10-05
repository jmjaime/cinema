package com.cinema.domain.movie

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.math.BigDecimal

class IMDBRatingTest {

    @Test
    fun `can create valid rating`() {
        IMDBRating(rating = BigDecimal.valueOf(5), votes = 10)
    }

    @Test
    fun `cannot create rating with invalid votes`() {
        val error = assertThrows<IllegalStateException> { IMDBRating(rating = BigDecimal.valueOf(5), votes = -3) }
        Assertions.assertEquals("votes should be positive", error.message)
    }

    @Test
    fun `cannot create rating lower than 0`() {
        val error = assertThrows<IllegalStateException> { IMDBRating(rating = BigDecimal.valueOf(-1), votes = 8) }
        Assertions.assertEquals("Rating should be between 0 and 10", error.message)

    }

    @Test
    fun `cannot create rating gather than 10`() {
        val error = assertThrows<IllegalStateException> { IMDBRating(rating = BigDecimal.valueOf(30), votes = 8) }
        Assertions.assertEquals("Rating should be between 0 and 10", error.message)
    }
}