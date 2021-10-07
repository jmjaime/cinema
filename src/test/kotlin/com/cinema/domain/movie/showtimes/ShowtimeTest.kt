package com.cinema.domain.movie.showtimes

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.math.BigDecimal
import java.time.DayOfWeek
import java.time.LocalTime

class ShowtimeTest {

    @Test
    fun `can create valid showtime`() {
        Showtime(
            dayOfWeek = DayOfWeek.SATURDAY,
            startAt = LocalTime.now(),
            price = BigDecimal.TEN
        )
    }

    @Test
    fun `showtime price should be positive`() {
        val error = assertThrows<IllegalStateException> {
            Showtime(
                dayOfWeek = DayOfWeek.THURSDAY,
                startAt = LocalTime.now(),
                price = BigDecimal.valueOf(-1)
            )
        }
        Assertions.assertEquals("price should be grater than 0", error.message)
    }
}