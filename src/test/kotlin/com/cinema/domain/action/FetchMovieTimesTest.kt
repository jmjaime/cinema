package com.cinema.domain.action

import com.cinema.anyString
import com.cinema.domain.actions.FetchMovieTimes
import com.cinema.domain.errors.MovieNotFound
import com.cinema.domain.movie.MovieLocator
import com.cinema.domain.movie.Price
import com.cinema.domain.movie.showtimes.DailyShowtime
import com.cinema.domain.movie.showtimes.MovieProjection
import com.cinema.domain.movie.showtimes.Showtime
import com.cinema.givenPersistedMovie
import com.cinema.infrastructure.persistence.memory.InMemoryDailyShowtimes
import com.cinema.infrastructure.persistence.memory.InMemoryMovies
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.math.BigDecimal
import java.time.*
import java.time.temporal.TemporalAdjusters

class FetchMovieTimesTest {

    companion object {
        private val now = LocalDate.of(2021, 10, 8).atStartOfDay(ZoneId.systemDefault()).toInstant()
    }

    private lateinit var movies: InMemoryMovies
    private lateinit var dailyShowtimes: InMemoryDailyShowtimes
    private lateinit var fetchMovieTimes: FetchMovieTimes
    private lateinit var clock: Clock

    @BeforeEach
    fun setUp() {
        movies = InMemoryMovies()
        dailyShowtimes = InMemoryDailyShowtimes()
        clock = Clock.fixed(now, ZoneId.systemDefault())
        fetchMovieTimes = FetchMovieTimes(MovieLocator(movies), dailyShowtimes, clock)
    }

    @Test
    fun `can fetch movie times`() {
        val movie = givenPersistedMovie(movies)
        val timeTable = listOf(
            DailyShowtime(
                movieId = movie.imdbId,
                day = DayOfWeek.MONDAY,
                showtimes = listOf(Showtime(startAt = LocalTime.now(), price = Price(BigDecimal.ONE)))
            ),
            DailyShowtime(
                movieId = movie.imdbId,
                day = DayOfWeek.FRIDAY,
                showtimes = listOf(Showtime(startAt = LocalTime.now(), price = Price(BigDecimal.TEN)))
            )
        ).onEach {
            dailyShowtimes.save(it)
        }


        val result = fetchMovieTimes(FetchMovieTimes.Request(movieId = movie.imdbId))

        val expectedMovieProjections = expectedMovieProjections(timeTable)
        Assertions.assertEquals(expectedMovieProjections, result)
    }

    @Test
    fun `when movie does not exit, fails with MovieNotFount`() {
        val id = anyString()
        val error = assertThrows<MovieNotFound> {
            fetchMovieTimes(FetchMovieTimes.Request(movieId = id))
        }
        Assertions.assertEquals("Movie $id not found", error.message)

    }

    @Test
    fun `when there are not showtimes, it returns empty`() {
        val movie = givenPersistedMovie(movies)
        val result = fetchMovieTimes(FetchMovieTimes.Request(movieId = movie.imdbId))

        Assertions.assertTrue(result.isEmpty())
    }

    private fun expectedMovieProjections(dailyShowtimes: List<DailyShowtime>) =
        dailyShowtimes.flatMap { dsht ->
            dsht.showtimes.map { sht ->
                MovieProjection(
                    startAt = LocalDate.now(clock).atTime(sht.startAt).with(TemporalAdjusters.nextOrSame(dsht.day)),
                    price = sht.price
                )
            }
        }.sortedBy { it.startAt }
}

