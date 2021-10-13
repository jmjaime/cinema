package com.cinema.infrastructure.routes

import com.cinema.anyMovie
import com.cinema.anyString
import com.cinema.domain.movie.Price
import com.cinema.domain.movie.showtimes.Showtime
import com.cinema.infrastructure.persistence.memory.InMemoryMovies
import com.cinema.infrastructure.rest.handlers.SaveMovieShowtimeHandler
import com.cinema.modulesForTest
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.server.testing.*
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.koin.test.KoinTest
import org.koin.test.inject
import java.math.BigDecimal
import java.time.LocalTime

class SaveMovieShowtimeRoute : KoinTest {

    @Test
    fun `can save movie showtime`() {
        withTestApplication(Application::modulesForTest) {
            val movie = givenMovie()
            val request = anySaveShowtimeRequest()
            with(
                handleRequest(HttpMethod.Post, "/movies/${movie.imdbId}/showtime/monday") {
                    addHeader(HttpHeaders.ContentType, ContentType.Application.Json.toString())
                    setBody(Json.encodeToString(request))
                }
            ) {
                val expectedShowtimes =
                    request.showTimeRequests.map { Showtime(startAt = it.time, price = Price(it.price)) }
                Assertions.assertEquals(HttpStatusCode.OK, response.status())
                Assertions.assertEquals(bodyFrom(expectedShowtimes), response.content)

            }
        }
    }

    @Test
    fun `returns not found if movie does not exist`() {
        withTestApplication(Application::modulesForTest) {
            with(
                handleRequest(HttpMethod.Post, "/movies/${anyString()}/showtime/monday") {
                    addHeader(HttpHeaders.ContentType, ContentType.Application.Json.toString())
                    setBody(Json.encodeToString(anySaveShowtimeRequest()))
                }
            ) {
                Assertions.assertEquals(HttpStatusCode.NotFound, response.status())
            }
        }
    }

    private fun givenMovie() = anyMovie().apply {
        val movies: InMemoryMovies by inject()
        movies.save(this)
    }

    private fun anySaveShowtimeRequest(time: LocalTime = LocalTime.now()) =
        SaveMovieShowtimeHandler.Request(
            listOf(SaveMovieShowtimeHandler.ShowTimeRequest(time, BigDecimal.TEN))
        )

    private fun bodyFrom(showtime: List<Showtime>): String {
        val result = showtime.joinToString(",") { """{"startAt":"${it.startAt}","price":"${it.price.amount}"}""" } ?: ""
        return """[$result]"""
    }

}

