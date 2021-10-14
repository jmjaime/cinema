package com.cinema.infrastructure.routes.cinema

import com.cinema.anyMovie
import com.cinema.anyString
import com.cinema.domain.movie.Movie
import com.cinema.domain.rating.CustomerVote
import com.cinema.domain.rating.CustomerVotes
import com.cinema.domain.rating.Rating
import com.cinema.infrastructure.persistence.memory.InMemoryMovies
import com.cinema.infrastructure.rest.handlers.cinema.RateMovieHandler
import com.cinema.modulesForTest
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.server.testing.*
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.koin.test.KoinTest
import org.koin.test.inject

class RateMovieRouteTest : KoinTest {

    @Test
    fun `can rate a movie`() {
        withTestApplication(Application::modulesForTest) {
            val movie = givenMovie()
            with(
                handleRequest(HttpMethod.Post, "/movies/${movie.imdbId}/rate") {
                    addHeader(HttpHeaders.ContentType, ContentType.Application.Json.toString())
                    setBody(Json.encodeToString(anyRateRequest(3)))
                }
            ) {
                assertEquals(HttpStatusCode.Created, response.status())
            }
        }
    }

    @Test
    fun `returns not found if movie does not exist`() {
        withTestApplication(Application::modulesForTest) {
            with(
                handleRequest(HttpMethod.Post, "/movies/${anyString()}/rate") {
                    addHeader(HttpHeaders.ContentType, ContentType.Application.Json.toString())
                    setBody(Json.encodeToString(anyRateRequest(3)))
                }
            ) {
                assertEquals(HttpStatusCode.NotFound, response.status())
            }
        }
    }

    @Test
    fun `cannot rate a movie two times`() {
        withTestApplication(Application::modulesForTest) {
            val movie = givenMovie()
            val user = anyString()
            giveRateFor(movie, user)
            with(
                handleRequest(HttpMethod.Post, "/movies/${movie.imdbId}/rate") {
                    addHeader(HttpHeaders.ContentType, ContentType.Application.Json.toString())
                    setBody(Json.encodeToString(anyRateRequest(rate = 1, user = user)))
                }
            ) {
                assertEquals(HttpStatusCode.Conflict, response.status())
            }
        }
    }

    private fun givenMovie() = anyMovie().apply {
        val movies: InMemoryMovies by inject()
        movies.save(this)
    }

    private fun anyRateRequest(rate: Int, user: String = anyString()) =
        RateMovieHandler.Request(user, rate, anyString())

    private fun giveRateFor(movie: Movie, user: String) {
        val customerVotes: CustomerVotes by inject()
        customerVotes.save(CustomerVote(user, movie.imdbId, Rating.FOUR, anyString()))
    }

}


