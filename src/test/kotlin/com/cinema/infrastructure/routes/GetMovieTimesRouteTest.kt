package com.cinema.infrastructure.routes

import com.cinema.anyMovie
import com.cinema.anyShowtime
import com.cinema.anyString
import com.cinema.domain.movie.Movie
import com.cinema.domain.movie.showtimes.MovieSchedule
import com.cinema.domain.movie.showtimes.Showtime
import com.cinema.infrastructure.persistence.memory.InMemoryMovieSchedules
import com.cinema.infrastructure.persistence.memory.InMemoryMovies
import com.cinema.modulesForTest
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.server.testing.*
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.koin.test.KoinTest
import org.koin.test.inject
import java.time.Clock
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.temporal.TemporalAdjusters

class GetMovieTimesRouteTest : KoinTest {

    @Test
    fun `returns available movie times`() {
        withTestApplication(Application::modulesForTest) {
            val movie = givenMovie()
            val movieSchedule = givenMovieSchedule(movie)
            handleRequest(HttpMethod.Get, "/movies/${movie.imdbId}/times").apply {
                assertEquals(HttpStatusCode.OK, response.status())
                assertEquals(bodyFrom(movieSchedule), response.content)
            }
        }
    }

    @Test
    fun `returns empty list when there is not any movie time`() {
        withTestApplication(Application::modulesForTest) {
            val movie = givenMovie()
            handleRequest(HttpMethod.Get, "/movies/${movie.imdbId}/times").apply {
                assertEquals(HttpStatusCode.OK, response.status())
                assertEquals(bodyFrom(), response.content)
            }
        }
    }

    @Test
    fun `returns not found if movie does not exist`() {
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

    private fun givenMovieSchedule(movie: Movie): MovieSchedule = MovieSchedule(
        movieId = movie.imdbId,
        showtimes = mutableListOf(anyShowtime())
    ).apply {
        val movieSchedules: InMemoryMovieSchedules by inject()
        movieSchedules.save(this)
    }


    private fun bodyFrom(movieSchedule: MovieSchedule? = null): String {
        val result = movieSchedule?.showtimes()
            ?.joinToString(",") { """{"startAt":"${projectionDateTimeFrom(it)}","price":"${it.price.amount}"}""" } ?: ""
        return """[$result]"""
    }

    private fun projectionDateTimeFrom(showtime: Showtime): LocalDateTime {
        val clock: Clock by inject()
        return LocalDate.now(clock).atTime(showtime.startAt).with(TemporalAdjusters.nextOrSame(showtime.dayOfWeek))
    }

}


