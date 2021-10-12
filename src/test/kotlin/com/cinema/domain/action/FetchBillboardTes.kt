package com.cinema.domain.action

import com.cinema.anyShowtime
import com.cinema.domain.actions.FetchBillboard
import com.cinema.domain.billboard.Billboard
import com.cinema.infrastructure.persistence.memory.InMemoryMovies
import com.cinema.domain.movie.Movie
import com.cinema.infrastructure.persistence.memory.InMemoryMovieSchedules
import com.cinema.domain.movie.showtimes.MovieSchedule
import com.cinema.givenPersistedMovie
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class FetchBillboardTes {

    private lateinit var movies: InMemoryMovies
    private lateinit var movieSchedules: InMemoryMovieSchedules

    private lateinit var fetchBillboard: FetchBillboard

    @BeforeEach
    fun setUp() {
        movies = InMemoryMovies()
        movieSchedules = InMemoryMovieSchedules()
        fetchBillboard = FetchBillboard(movieSchedules, movies)
    }

    @Test
    fun `can fetch billboard`() {
        val availableMovies = listOf(givenPersistedMovie(movies), givenPersistedMovie(movies))
        availableMovies.forEach { givenScheduleFor(it) }

        val billboard = fetchBillboard()

        Assertions.assertEquals(Billboard(availableMovies), billboard)
    }

    @Test
    fun `can fetch empty billboard`() {
        givenPersistedMovie(movies)
        val billboard = fetchBillboard()
        Assertions.assertEquals(Billboard(emptyList()), billboard)
    }

    private fun givenScheduleFor(movie: Movie) = MovieSchedule(
        movieId = movie.imdbId,
        showtimes = mutableListOf(anyShowtime())
    ).apply {
        movieSchedules.save(this)
    }


}