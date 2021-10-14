package com.cinema.infrastructure.routes

import com.cinema.anyDailyShowtime
import com.cinema.anyMovie
import com.cinema.anyString
import com.cinema.domain.movie.Movie
import com.cinema.domain.movie.Price
import com.cinema.domain.movie.showtimes.DailyShowtime
import com.cinema.domain.movie.showtimes.DailyShowtimes
import com.cinema.domain.movie.showtimes.Showtime
import com.cinema.infrastructure.persistence.memory.InMemoryMovies
import com.cinema.modulesForTest
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.server.testing.*
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.koin.test.KoinTest
import org.koin.test.inject

class FetchMovieShowtimeRoute : KoinTest {

    @Test
    fun `can fetch daily movie showtime`() {
        withTestApplication(Application::modulesForTest) {
            val movie = givenMovie()
            val dailyShowtime = givenDailyShowTime(movie)
            with(
                handleRequest(HttpMethod.Get, "/movies/${movie.imdbId}/showtime/${dailyShowtime.day.name}") {
                    addHeader(HttpHeaders.ContentType, ContentType.Application.Json.toString())
                }
            ) {
                Assertions.assertEquals(HttpStatusCode.OK, response.status())
                Assertions.assertEquals(bodyFrom(dailyShowtime), response.content)
            }
        }
    }

    @Test
    fun `returns not found if movie does not exist`() {
        withTestApplication(Application::modulesForTest) {
            with(
                handleRequest(HttpMethod.Get, "/movies/${anyString()}/showtime/monday") {
                    addHeader(HttpHeaders.ContentType, ContentType.Application.Json.toString())
                }
            ) {
                Assertions.assertEquals(HttpStatusCode.NotFound, response.status())
            }
        }
    }

    @Test
    fun `returns not found whe day of week is invalid`() {
        withTestApplication(Application::modulesForTest) {
            val movie = givenMovie()
            with(
                handleRequest(HttpMethod.Get, "/movies/${movie.imdbId}/showtime/${anyString()}") {
                    addHeader(HttpHeaders.ContentType, ContentType.Application.Json.toString())
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

    private fun givenDailyShowTime(movie: Movie) = anyDailyShowtime(movieId = movie.imdbId).apply {
        val dailyShowtimes: DailyShowtimes by inject()
        dailyShowtimes.save(this)
    }


    private fun bodyFrom(dailyShowtime: DailyShowtime): String {
        val showtimes =
            dailyShowtime.showtimes.joinToString(",") { """{"startAt":"${it.startAt}","price":"${it.price.amount}"}""" }
                ?: ""
        return """{"movieId":"${dailyShowtime.movieId}","day":"${dailyShowtime.day}","showtimes":[$showtimes]}"""
    }

}

