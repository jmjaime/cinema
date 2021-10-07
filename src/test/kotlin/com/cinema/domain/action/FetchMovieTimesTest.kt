package com.cinema.domain.action

import com.cinema.anyMovie
import com.cinema.anyString
import com.cinema.domain.actions.FetchMovieTimes
import com.cinema.domain.errors.MovieNotFound
import com.cinema.domain.movie.InMemoryMovies
import com.cinema.domain.movie.Movie
import com.cinema.domain.movie.showtimes.InMemoryMovieSchedules
import com.cinema.domain.movie.showtimes.MovieSchedule
import com.cinema.domain.movie.showtimes.Showtime
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.math.BigDecimal
import java.time.DayOfWeek
import java.time.LocalTime

class FetchMovieTimesTest {
    private lateinit var movies: InMemoryMovies
    private lateinit var movieSchedules: InMemoryMovieSchedules
    private lateinit var fetchMovieTimes: FetchMovieTimes

    @BeforeEach
    fun setUp() {
        movies = InMemoryMovies()
        movieSchedules = InMemoryMovieSchedules()
        fetchMovieTimes = FetchMovieTimes(movies, movieSchedules)
    }

    @Test
    fun `can fetch movie times`() {
        val id = anyString()
        val mondayShowtime = Showtime(dayOfWeek = DayOfWeek.MONDAY, startAt = LocalTime.now(), price = BigDecimal.ONE)
        val fridayShowtime = Showtime(dayOfWeek = DayOfWeek.FRIDAY, startAt = LocalTime.now(), price = BigDecimal.ONE)
        val movie = givenMovie(id = id)
        val movieSchedule = givenSchedule(movie, listOf(mondayShowtime, fridayShowtime))

        val result = fetchMovieTimes(FetchMovieTimes.Request(movieId = id))

        Assertions.assertEquals(movieSchedule.showTimes(), result)
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
        val id = anyString()
        givenMovie(id = id)
       val result = fetchMovieTimes(FetchMovieTimes.Request(movieId = id))

        Assertions.assertTrue(result.isEmpty())
    }

    private fun givenMovie(id: String): Movie = anyMovie(id = id).also { movies.save(it) }

    private fun givenSchedule(movie: Movie, showtimes: List<Showtime>) =
        MovieSchedule(
            movieId = movie.imdbId,
            showtimes = showtimes.toMutableList()
        ).apply {
            movieSchedules.save(this)
        }
}

