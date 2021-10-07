package com.cinema.domain.action

import com.cinema.anyMovie
import com.cinema.anyShowtime
import com.cinema.anyString
import com.cinema.domain.actions.FetchBillboard
import com.cinema.domain.billboard.Billboard
import com.cinema.domain.movie.InMemoryMovies
import com.cinema.domain.movie.Movie
import com.cinema.domain.movie.showtimes.InMemoryMovieSchedules
import com.cinema.domain.movie.showtimes.MovieSchedule
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
        val availableMovies = listOf(givenMovie(anyString()), givenMovie(anyString()))
        availableMovies.forEach { givenScheduleFor(it) }

        val billboard = fetchBillboard()

        Assertions.assertEquals(Billboard(availableMovies), billboard)
    }

    @Test
    fun `can fetch empty billboard`() {
        givenMovie(anyString())
        val billboard = fetchBillboard()
        Assertions.assertEquals(Billboard(emptyList()), billboard)
    }

    private fun givenScheduleFor(movie: Movie) = MovieSchedule(
        movieId = movie.imdbId,
        showtimes = mutableListOf(anyShowtime())
    ).apply {
        movieSchedules.save(this)
    }


    private fun givenMovie(id: String): Movie = anyMovie(id = id).also { movies.save(it) }

}