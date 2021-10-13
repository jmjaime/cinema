package com.cinema.domain.action

import com.cinema.anyDailyShowtime
import com.cinema.anyShowtime
import com.cinema.anyString
import com.cinema.domain.actions.SaveMovieShowtime
import com.cinema.domain.errors.MovieNotFound
import com.cinema.domain.movie.Movie
import com.cinema.domain.movie.MovieLocator
import com.cinema.domain.movie.Price
import com.cinema.domain.movie.showtimes.DailyShowtime
import com.cinema.domain.movie.showtimes.Showtime
import com.cinema.givenPersistedMovie
import com.cinema.infrastructure.persistence.memory.InMemoryDailyShowtimes
import com.cinema.infrastructure.persistence.memory.InMemoryMovies
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.math.BigDecimal.TEN
import java.time.DayOfWeek
import java.time.LocalTime

class SaveMovieShowTimeTest {

    private lateinit var movies: InMemoryMovies
    private lateinit var inMemoryDailyShowtimes: InMemoryDailyShowtimes
    private lateinit var saveMovieShowTime: SaveMovieShowtime

    @BeforeEach
    fun setUp() {
        movies = InMemoryMovies()
        inMemoryDailyShowtimes = InMemoryDailyShowtimes()
        saveMovieShowTime = SaveMovieShowtime(MovieLocator(movies), inMemoryDailyShowtimes)
    }

    @Test
    fun `cannot save showtime for unknown movie`() {
        val unknownId = anyString()
        val error = assertThrows<MovieNotFound> {
            saveMovieShowTime(
                SaveMovieShowtime.Request(
                    unknownId,
                    DayOfWeek.MONDAY,
                    listOf(LocalTime.now() to Price(TEN))
                )
            )
        }
        Assertions.assertEquals("Movie $unknownId not found", error.message)

    }

    @Test
    fun `can save daily showtime for a movie`() {
        val dayOfWeek = DayOfWeek.MONDAY
        val movie = givenPersistedMovie(movies)
        val showtime = anyShowtime()
        val request = SaveMovieShowtime.Request(movie.imdbId, dayOfWeek, listOf(showtime.startAt to showtime.price))

        val result = saveMovieShowTime(request)

        assertShowtimeCreated(dayOfWeek, result, request)
        val expectedDailyShowtime = DailyShowtime(movieId = movie.imdbId, day = dayOfWeek, showtimes = listOf(showtime))
        assertMovieDailyShowtime(movie, expectedDailyShowtime)
    }

    @Test
    fun `can add daily showtime for a movie`() {
        val dayOfWeek = DayOfWeek.THURSDAY
        val movie = givenPersistedMovie(movies)
        val showtime = givenDailyShowTime(movie)
        val newShowtime = anyShowtime()
        val request =
            SaveMovieShowtime.Request(movie.imdbId, dayOfWeek, listOf(newShowtime.startAt to newShowtime.price))

        val result = saveMovieShowTime(request)

        assertShowtimeCreated(dayOfWeek, result, request)
        val expectedDailyShowtime =
            DailyShowtime(movieId = movie.imdbId, day = dayOfWeek, showtimes = listOf(newShowtime))
        assertMovieDailyShowtime(movie, expectedDailyShowtime)
    }


    private fun givenDailyShowTime(movie: Movie) = anyDailyShowtime(movieId = movie.imdbId).apply {
        inMemoryDailyShowtimes.save(this)
    }

    private fun assertMovieDailyShowtime(
        movie: Movie,
        expectedDailyShowtime: DailyShowtime
    ) {
        val dailyShowtime = inMemoryDailyShowtimes.findByMovieIdAndDay(movie.imdbId, expectedDailyShowtime.day)
        Assertions.assertNotNull(dailyShowtime)
        Assertions.assertEquals(expectedDailyShowtime, dailyShowtime)
    }

    private fun assertShowtimeCreated(
        dayOfWeek: DayOfWeek,
        result: DailyShowtime,
        request: SaveMovieShowtime.Request
    ) {
        Assertions.assertEquals(dayOfWeek, result.day)
        Assertions.assertEquals(
            request.showTimes.map { Showtime(it.first, it.second) },
            result.showtimes
        )
    }

}
