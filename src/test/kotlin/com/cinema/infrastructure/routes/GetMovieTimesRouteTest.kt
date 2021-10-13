package com.cinema.infrastructure.routes

import com.cinema.*
import com.cinema.domain.movie.Movie
import com.cinema.domain.movie.showtimes.DailyShowtime
import com.cinema.domain.movie.showtimes.Showtime
import com.cinema.infrastructure.persistence.memory.InMemoryDailyShowtimes
import com.cinema.infrastructure.persistence.memory.InMemoryMovies
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.server.testing.*
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.koin.test.KoinTest
import org.koin.test.inject
import java.time.Clock
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.temporal.TemporalAdjusters

class GetMovieTimesRouteTest : KoinTest {

    @Test
    fun `returns available movie times`() {
        withTestApplication(Application::modulesForTest) {
            val movie = givenMovie()
            val dailyShowtime = givenDailyShowtime(movie)
            handleRequest(HttpMethod.Get, "/movies/${movie.imdbId}/times").apply {
                assertEquals(HttpStatusCode.OK, response.status())
                assertEquals(bodyFrom(dailyShowtime), response.content)
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

    private fun givenDailyShowtime(movie: Movie): DailyShowtime = anyDailyShowtime(movie.imdbId).apply {
        val dailyShowtimes: InMemoryDailyShowtimes by inject()
        dailyShowtimes.save(this)
    }


    private fun bodyFrom(dailyShowtime: DailyShowtime? = null): String {
        val result = dailyShowtime?.showtimes
            ?.joinToString(",") { """{"startAt":"${projectionDateTimeFrom(dailyShowtime.day,it)}","price":"${it.price.amount}"}""" } ?: ""
        return """[$result]"""
    }

    private fun projectionDateTimeFrom(dayOfWeek: DayOfWeek,showtime: Showtime): LocalDateTime {
        val clock: Clock by inject()
        return LocalDate.now(clock).atTime(showtime.startAt).with(TemporalAdjusters.nextOrSame(dayOfWeek))
    }

}


