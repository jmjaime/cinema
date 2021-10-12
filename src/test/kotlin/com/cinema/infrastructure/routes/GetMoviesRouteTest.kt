package com.cinema.infrastructure.routes

import com.cinema.anyMovie
import com.cinema.domain.movie.Movie
import com.cinema.infrastructure.persistence.memory.InMemoryMovies
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.server.testing.*
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.koin.test.KoinTest
import org.koin.test.inject

class GetMoviesRouteTest : KoinTest {

    @Test
    fun `returns available movies`() {
        withTestApplication(Application::modulesForTest) {
            val movies: InMemoryMovies by inject()
            val availableMovies = givenMovies(movies)
            handleRequest(HttpMethod.Get, "/movies").apply {
                assertEquals(HttpStatusCode.OK, response.status())
                assertEquals(bodyFrom(availableMovies), response.content)
            }
        }
    }

    @Test
    fun `returns empty list when there is not any movie`() {
        withTestApplication(Application::modulesForTest) {
            handleRequest(HttpMethod.Get, "/movies").apply {
                assertEquals(HttpStatusCode.OK, response.status())
                assertEquals(bodyFrom(emptyList()), response.content)
            }
        }
    }

    private fun givenMovies(movies: InMemoryMovies): List<Movie> =
        listOf(anyMovie(), anyMovie()).onEach { movies.save(it) }

    private fun bodyFrom(availableMovies: List<Movie>): String {
        val result = availableMovies.joinToString(",") { """{"id":"${it.imdbId}","name":"${it.name}"}""" }
        return """[$result]"""
    }
}


