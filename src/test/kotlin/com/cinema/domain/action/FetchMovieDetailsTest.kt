package com.cinema.domain.action

import com.cinema.anyString
import com.cinema.domain.actions.FetchMovieDetails
import com.cinema.domain.errors.MovieNotFound
import com.cinema.domain.movie.InMemoryMovies
import com.cinema.domain.movie.MovieLocator
import com.cinema.givenPersistedMovie
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class FetchMovieDetailsTest {

    private lateinit var movies: InMemoryMovies
    private lateinit var fetchMovieDetails: FetchMovieDetails

    @BeforeEach
    fun setUp() {
        movies = InMemoryMovies()
        fetchMovieDetails = FetchMovieDetails(MovieLocator(movies))
    }

    @Test
    fun `can fetch movie details`() {
        val movie = givenPersistedMovie(movies)

        val result = fetchMovieDetails(FetchMovieDetails.Request(id = movie.imdbId))

        Assertions.assertEquals(movie, result)
    }

    @Test
    fun `when movie does not exit, fails with MovieNotFount`() {
        val id = anyString()
        val error = assertThrows<MovieNotFound> {
            fetchMovieDetails(FetchMovieDetails.Request(id = id))
        }
        Assertions.assertEquals("Movie $id not found", error.message)

    }

}