package com.cinema.domain.movie.showtimes

import com.cinema.anyString
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.math.BigDecimal
import java.time.LocalDateTime

class ShowtimeTest {

    @Test
    fun `can create valid showtime`() {
        Showtime(
            id = anyString(),
            startAt = LocalDateTime.now(),
            price = BigDecimal.TEN
        )
    }

    @Test
    fun `showtime id is mandatory`() {
        val error = assertThrows<IllegalStateException> {
            Showtime(
                id = "",
                startAt = LocalDateTime.now(),
                price = BigDecimal.TEN
            )
        }
        Assertions.assertEquals("id is mandatory", error.message)
    }

    @Test
    fun `showtime price should be positive`() {
        val error = assertThrows<IllegalStateException> {
            Showtime(
                id = anyString(),
                startAt = LocalDateTime.now(),
                price = BigDecimal.valueOf(-1)
            )
        }
        Assertions.assertEquals("price should be grater than 0", error.message)
    }
}