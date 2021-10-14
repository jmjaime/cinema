package com.cinema.domain.action

import com.cinema.anyDailyShowtime
import com.cinema.anyString
import com.cinema.domain.actions.FetchMovieShowtime
import com.cinema.domain.errors.MovieNotFound
import com.cinema.domain.movie.Movie
import com.cinema.domain.movie.MovieLocator
import com.cinema.domain.movie.showtimes.DailyShowtime
import com.cinema.givenPersistedMovie
import com.cinema.infrastructure.persistence.memory.InMemoryDailyShowtimes
import com.cinema.infrastructure.persistence.memory.InMemoryMovies
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.time.DayOfWeek

class FetchMovieShowtimeTest {

    private lateinit var movies: InMemoryMovies
    private lateinit var inMemoryDailyShowtimes: InMemoryDailyShowtimes
    private lateinit var fetchMovieShowtime: FetchMovieShowtime

    @BeforeEach
    fun setUp() {
        movies = InMemoryMovies()
        inMemoryDailyShowtimes = InMemoryDailyShowtimes()
        fetchMovieShowtime = FetchMovieShowtime(MovieLocator(movies), inMemoryDailyShowtimes)
    }

    @Test
    fun `cannot return daily showtime for unknown movie`() {
        val unknownId = anyString()
        val error = assertThrows<MovieNotFound> {
            fetchMovieShowtime(
                FetchMovieShowtime.Request(
                    unknownId,
                    DayOfWeek.MONDAY
                )
            )
        }
        Assertions.assertEquals("Movie $unknownId not found", error.message)

    }

    @Test
    fun `can fetch daily showtime for a movie`() {
        val movie = givenPersistedMovie(movies)
       val  dailyShowtime = givenDailyShowTime(movie)

        val result = fetchMovieShowtime(FetchMovieShowtime.Request(dailyShowtime.movieId, dailyShowtime.day))

        Assertions.assertEquals(dailyShowtime, result)
    }

    @Test
    fun `can fetch daily showtime for a movie when there is not ny showtime`() {
        val dayOfWeek = DayOfWeek.THURSDAY
        val movie = givenPersistedMovie(movies)
        val  dailyShowtime = DailyShowtime.dailyShowtimeFor(movie,dayOfWeek)

        val result = fetchMovieShowtime(FetchMovieShowtime.Request(movie.imdbId, dayOfWeek))

        Assertions.assertEquals(dailyShowtime, result)
    }

    private fun givenDailyShowTime(movie: Movie) = anyDailyShowtime(movieId = movie.imdbId).apply {
        inMemoryDailyShowtimes.save(this)
    }

}
