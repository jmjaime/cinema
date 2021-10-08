package com.cinema.domain.action

import com.cinema.anyMovie
import com.cinema.anyString
import com.cinema.domain.actions.FetchMovieTimes
import com.cinema.domain.errors.MovieNotFound
import com.cinema.domain.movie.InMemoryMovies
import com.cinema.domain.movie.Movie
import com.cinema.domain.movie.Price
import com.cinema.domain.movie.showtimes.InMemoryMovieSchedules
import com.cinema.domain.movie.showtimes.MovieProjection
import com.cinema.domain.movie.showtimes.MovieSchedule
import com.cinema.domain.movie.showtimes.Showtime
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
    private lateinit var movieSchedules: InMemoryMovieSchedules
    private lateinit var fetchMovieTimes: FetchMovieTimes
    private lateinit var clock: Clock

    @BeforeEach
    fun setUp() {
        movies = InMemoryMovies()
        movieSchedules = InMemoryMovieSchedules()
        clock = Clock.fixed(now, ZoneId.systemDefault())
        fetchMovieTimes = FetchMovieTimes(movies, movieSchedules, clock)
    }

    @Test
    fun `can fetch movie times`() {
        val id = anyString()
        val mondayShowtime =
            Showtime(dayOfWeek = DayOfWeek.MONDAY, startAt = LocalTime.now(), price = Price(BigDecimal.ONE))
        val fridayShowtime =
            Showtime(dayOfWeek = DayOfWeek.FRIDAY, startAt = LocalTime.now(), price = Price(BigDecimal.TEN))
        val movie = givenMovie(id = id)
        val movieSchedule = givenSchedule(movie, listOf(mondayShowtime, fridayShowtime))

        val result = fetchMovieTimes(FetchMovieTimes.Request(movieId = id))

        val expectedMovieProjections = expectedMovieProjections(movieSchedule)
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

    private fun expectedMovieProjections(movieSchedule: MovieSchedule) =
        movieSchedule.showtimes().map {
            MovieProjection(
                startAt = LocalDate.now(clock).atTime(it.startAt).with(TemporalAdjusters.nextOrSame(it.dayOfWeek)),
                price = it.price
            )
        }.sortedBy { it.startAt }
}

