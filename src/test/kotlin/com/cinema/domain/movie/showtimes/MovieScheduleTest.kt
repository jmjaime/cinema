package com.cinema.domain.movie.showtimes

import com.cinema.anyShowtime
import com.cinema.anyString
import com.cinema.domain.errors.ShowTimeAlreadyExists
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class MovieScheduleTest {


    @Test
    fun `can create valid movie schedule`() {
        MovieSchedule(
            movieId = anyString(),
            showtimes = mutableListOf(anyShowtime())
        )
    }

    @Test
    fun `movie id is mandatory`() {
        val error = assertThrows<IllegalStateException> {
            MovieSchedule(
                movieId = "",
                showtimes = mutableListOf()
            )
        }
        Assertions.assertEquals("movieId is mandatory", error.message)
    }

    @Test
    fun `can add showtime`() {
        val showtime1 = anyShowtime()
        val showtime2 = showtime1.copy(startAt = showtime1.startAt.plusHours(4))
        val schedule = MovieSchedule(movieId = anyString(), showtimes = mutableListOf(showtime1))

        schedule.addShowtime(showtime2)

        Assertions.assertEquals(listOf(showtime1, showtime2), schedule.showtimes())
    }

    @Test
    fun `can add same showtime two time`() {
        val showtime = anyShowtime()
        val schedule = MovieSchedule(movieId = anyString(), showtimes = mutableListOf(showtime))
        val error = assertThrows<ShowTimeAlreadyExists> {
            schedule.addShowtime(showtime)
        }
        Assertions.assertEquals("Already exists showtime for movie ${schedule.movieId} at ${showtime.dayOfWeek} ${showtime.startAt}", error.message)

    }
}