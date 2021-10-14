package com.cinema.infrastructure.routes

import com.cinema.anyMovie
import com.cinema.anyString
import com.cinema.domain.movie.Movie
import com.cinema.infrastructure.persistence.memory.InMemoryMovies
import com.cinema.modulesForTest
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.server.testing.*
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.koin.test.KoinTest
import org.koin.test.inject

class GetMovieDetailsRouteTest : KoinTest {

    @Test
    fun `returns available movies`() {
        withTestApplication(Application::modulesForTest) {
            val movie = givenMovie()
            handleRequest(HttpMethod.Get, "/movies/${movie.imdbId}").apply {
                assertEquals(HttpStatusCode.OK, response.status())
                assertEquals(bodyFrom(movie), response.content)
            }
        }
    }

    @Test
    fun `returns empty list when there is not any movie`() {
        withTestApplication(Application::modulesForTest) {
            handleRequest(HttpMethod.Get, "/movies/${anyString()}").apply {
                assertEquals(HttpStatusCode.NotFound, response.status())
            }
        }
    }

    private fun givenMovie() = anyMovie().apply {
        val movies: InMemoryMovies by inject()
        movies.save(this)
    }

    private fun bodyFrom(movie: Movie) =
        """{"id":"${movie.imdbId}","name":"${movie.name}","description":"${movie.description}","releaseDate":"${movie.releaseDate}","runtime":"${movie.runtime}","imdbRating":"${movie.imdbRating.rating}"}"""


}


