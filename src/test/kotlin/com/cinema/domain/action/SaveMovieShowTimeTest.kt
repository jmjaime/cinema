package com.cinema.domain.action

import com.cinema.anyShowtime
import com.cinema.anyString
import com.cinema.domain.actions.SaveMovieShowTime
import com.cinema.domain.errors.MovieNotFound
import com.cinema.infrastructure.persistence.memory.InMemoryMovies
import com.cinema.domain.movie.Movie
import com.cinema.domain.movie.MovieLocator
import com.cinema.domain.movie.Price
import com.cinema.infrastructure.persistence.memory.InMemoryMovieSchedules
import com.cinema.domain.movie.showtimes.MovieSchedule
import com.cinema.domain.movie.showtimes.Showtime
import com.cinema.givenPersistedMovie
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.math.BigDecimal.TEN
import java.time.DayOfWeek
import java.time.LocalTime

class SaveMovieShowTimeTest {

    private lateinit var movies: InMemoryMovies
    private lateinit var movieSchedules: InMemoryMovieSchedules
    private lateinit var saveMovieShowTime: SaveMovieShowTime

    @BeforeEach
    fun setUp() {
        movies = InMemoryMovies()
        movieSchedules = InMemoryMovieSchedules()
        saveMovieShowTime = SaveMovieShowTime(MovieLocator(movies), movieSchedules)
    }

    @Test
    fun `cannot save showtime for unknown movie`() {
        val unknownId = anyString()
        val error = assertThrows<MovieNotFound> {
            saveMovieShowTime(SaveMovieShowTime.Request(unknownId, DayOfWeek.MONDAY, LocalTime.now(), TEN))
        }
        Assertions.assertEquals("Movie $unknownId not found", error.message)

    }

    @Test
    fun `can save first showtime for a movie`() {
        val dayOfWeek = DayOfWeek.MONDAY
        val movie = givenPersistedMovie(movies)
        val request = SaveMovieShowTime.Request(movie.imdbId, dayOfWeek, LocalTime.now(), TEN)

        val result = saveMovieShowTime(request)

        assertShowtimeCreated(dayOfWeek, result, request)
        assertMovieSchedule(movie, listOf(result))
    }

    @Test
    fun `can save a new showtime for a movie`() {
        val dayOfWeek = DayOfWeek.SATURDAY
        val movie = givenPersistedMovie(movies)
        val showtime = givenShowtime(movie)
        val request = SaveMovieShowTime.Request(movie.imdbId, dayOfWeek, LocalTime.now(), TEN)

        val result = saveMovieShowTime(request)

        assertShowtimeCreated(dayOfWeek, result, request)
        assertMovieSchedule(movie, listOf(showtime, result))
    }

    private fun givenShowtime(movie: Movie) = anyShowtime().also {
        MovieSchedule(
            movieId = movie.imdbId,
            showtimes = mutableListOf(it)
        ).apply {
            movieSchedules.save(this)
        }
    }

    private fun assertMovieSchedule(
        movie: Movie,
        expectedShowtimes: List<Showtime>
    ) {
        val movieSchedule = movieSchedules.findById(movie.imdbId)
        Assertions.assertNotNull(movieSchedule)
        with(checkNotNull(movieSchedule)) {
            Assertions.assertEquals(movie.imdbId, movieId)
            Assertions.assertEquals(expectedShowtimes, showtimes())
        }
    }

    private fun assertShowtimeCreated(
        dayOfWeek: DayOfWeek,
        result: Showtime,
        request: SaveMovieShowTime.Request
    ) {
        Assertions.assertEquals(dayOfWeek, result.dayOfWeek)
        Assertions.assertEquals(Price(request.price), result.price)
        Assertions.assertEquals(request.startAt, result.startAt)
    }

}
