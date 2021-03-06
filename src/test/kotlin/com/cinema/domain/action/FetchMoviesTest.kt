package com.cinema.domain.action

import com.cinema.domain.actions.FetchMovies
import com.cinema.infrastructure.persistence.memory.InMemoryMovies
import com.cinema.domain.movie.Movie
import com.cinema.givenPersistedMovie
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class FetchMoviesTest {

    private lateinit var movies: InMemoryMovies
    private lateinit var fetchMovies: FetchMovies

    @BeforeEach
    fun setUp() {
        movies = InMemoryMovies()
        fetchMovies = FetchMovies(movies = movies)
    }

    @Test
    fun `can fetch movie details`() {
        val availableMovies = givenMovies()

        val result = fetchMovies()

        Assertions.assertEquals(availableMovies, result)
    }

    @Test
    fun `can handle fetch movie details without movies`() {
        val result = fetchMovies()

        Assertions.assertTrue(result.isEmpty())
    }

    private fun givenMovies(length: Int = 3): List<Movie> {
        val availableMovies = mutableListOf<Movie>()
        repeat(length) {
            givenPersistedMovie(movies).apply {
                availableMovies.add(this)
            }
        }
        return availableMovies
    }


}