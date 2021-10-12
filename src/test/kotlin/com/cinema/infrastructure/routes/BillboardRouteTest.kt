package com.cinema.infrastructure.routes

import com.cinema.anyMovie
import com.cinema.anyMovieSchedule
import com.cinema.domain.movie.Movie
import com.cinema.infrastructure.persistence.memory.InMemoryMovieSchedules
import com.cinema.infrastructure.persistence.memory.InMemoryMovies
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.server.testing.*
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.koin.test.KoinTest
import org.koin.test.inject

class BillboardRouteTest : KoinTest {

    @Test
    fun `returns billboard with available movies`() {
        withTestApplication(Application::modulesForTest) {
            val availableMovies = givenMoviesInBillboard()
            handleRequest(HttpMethod.Get, "/billboard").apply {
                Assertions.assertEquals(HttpStatusCode.OK, response.status())
                Assertions.assertEquals(bodyFrom(availableMovies), response.content)
            }
        }
    }

    @Test
    fun `returns empty billboard`() {
        withTestApplication(Application::modulesForTest) {
            handleRequest(HttpMethod.Get, "/billboard").apply {
                Assertions.assertEquals(HttpStatusCode.OK, response.status())
                Assertions.assertEquals(bodyFrom(emptyList()), response.content)
            }
        }
    }


    private fun givenMoviesInBillboard(): List<Movie> {
        val movies: InMemoryMovies by inject()
        val movieSchedules: InMemoryMovieSchedules by inject()
        return listOf(anyMovie(), anyMovie()).onEach {
            movies.save(it)
            movieSchedules.save(anyMovieSchedule(movieId = it.imdbId))
        }
    }

    private fun bodyFrom(availableMovies: List<Movie>): String {
        val result = availableMovies.joinToString(",") { """{"id":"${it.imdbId}","name":"${it.name}"}""" }
        return """{"availableMovies":[$result]}"""
    }
}