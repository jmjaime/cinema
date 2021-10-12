package com.cinema.domain.movie

import com.cinema.anyString
import com.cinema.domain.errors.MovieNotFound
import com.cinema.givenPersistedMovie
import com.cinema.infrastructure.persistence.memory.InMemoryMovies
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class MovieLocatorTest {

    private lateinit var movies: InMemoryMovies
    private lateinit var movieLocator: MovieLocator

    @BeforeEach
    fun setUp() {
        movies = InMemoryMovies()
        movieLocator = MovieLocator(movies)
    }

    @Test
    fun `find movie fails when movie does not exist`() {
        val unknownId = anyString()
        val error = assertThrows<MovieNotFound> {
            movieLocator.findMovie(unknownId)
        }
        Assertions.assertEquals("Movie $unknownId not found", error.message)

    }

    @Test
    fun `can find movie by id`() {
        val movie = givenPersistedMovie(movies)
        val result = movieLocator.findMovie(movieId = movie.imdbId)
        Assertions.assertEquals(movie, result)

    }

}