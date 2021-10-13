package com.cinema.domain.movie.showtimes

import com.cinema.anyShowtime
import com.cinema.anyString
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.time.DayOfWeek

class DailyShowtimeTest {


    @Test
    fun `can create valid movie daily showtime`() {
        DailyShowtime(
            movieId = anyString(),
            day = DayOfWeek.FRIDAY,
            showtimes = mutableListOf(anyShowtime())
        )
    }

    @Test
    fun `movie id is mandatory`() {
        val error = assertThrows<IllegalStateException> {
            DailyShowtime(
                movieId = "",
                day = DayOfWeek.FRIDAY,
                showtimes = mutableListOf()
            )
        }
        Assertions.assertEquals("movieId is mandatory", error.message)
    }
}