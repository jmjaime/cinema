package com.cinema.domain.action

import com.cinema.anyDailyShowtime
import com.cinema.domain.actions.FetchBillboard
import com.cinema.domain.billboard.Billboard
import com.cinema.domain.movie.Movie
import com.cinema.givenPersistedMovie
import com.cinema.infrastructure.persistence.memory.InMemoryDailyShowtimes
import com.cinema.infrastructure.persistence.memory.InMemoryMovies
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class FetchBillboardTes {

    private lateinit var movies: InMemoryMovies
    private lateinit var dailyShowtimes: InMemoryDailyShowtimes

    private lateinit var fetchBillboard: FetchBillboard

    @BeforeEach
    fun setUp() {
        movies = InMemoryMovies()
        dailyShowtimes = InMemoryDailyShowtimes()
        fetchBillboard = FetchBillboard(dailyShowtimes, movies)
    }

    @Test
    fun `can fetch billboard`() {
        val availableMovies = listOf(givenPersistedMovie(movies), givenPersistedMovie(movies))
        availableMovies.forEach { givenDailyShowtime(it) }

        val billboard = fetchBillboard()

        Assertions.assertEquals(Billboard(availableMovies), billboard)
    }

    @Test
    fun `can fetch empty billboard`() {
        givenPersistedMovie(movies)
        val billboard = fetchBillboard()
        Assertions.assertEquals(Billboard(emptyList()), billboard)
    }

    private fun givenDailyShowtime(movie: Movie) = anyDailyShowtime(
        movieId = movie.imdbId
    ).apply {
        dailyShowtimes.save(this)
    }


}