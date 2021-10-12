package com.cinema.domain.action

import com.cinema.anyString
import com.cinema.domain.actions.RateMovie
import com.cinema.domain.errors.MovieAlreadyRated
import com.cinema.domain.errors.MovieNotFound
import com.cinema.infrastructure.persistence.memory.InMemoryMovies
import com.cinema.domain.movie.MovieLocator
import com.cinema.domain.rating.CustomerVotes
import com.cinema.infrastructure.persistence.memory.InMemoryCustomerVotes
import com.cinema.domain.rating.Rating
import com.cinema.givenPersistedMovie
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class RateMovieTest {

    private lateinit var movies: InMemoryMovies
    private lateinit var customerVotes: CustomerVotes
    private lateinit var rateMovie: RateMovie

    @BeforeEach
    fun setUp() {
        movies = InMemoryMovies()
        customerVotes = InMemoryCustomerVotes()
        rateMovie = RateMovie(MovieLocator(movies), customerVotes)
    }


    @Test
    fun `when movie does not exit, fails with MovieNotFount`() {
        val movieId = anyString()
        val error = assertThrows<MovieNotFound> {
            rateMovie(givenRateRequest(movieId))
        }
        Assertions.assertEquals("Movie $movieId not found", error.message)

    }

    @Test
    fun `customer cannot rate 2 times same movie`() {
        val movie = givenPersistedMovie(movies)
        val request = givenRateRequest(movie.imdbId)

        rateMovie(request)

        val error = assertThrows<MovieAlreadyRated> { rateMovie(request) }
        Assertions.assertEquals("Movie ${movie.imdbId} already rated", error.message)

    }

    private fun givenRateRequest(movieId: String) = RateMovie.Request(
        customer = anyString(),
        movieId = movieId,
        rating = Rating.FOUR,
        comment = anyString()
    )


}