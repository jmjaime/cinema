package com.cinema.infrastructure.routes

import com.cinema.anyMovie
import com.cinema.anyShowtime
import com.cinema.anyString
import com.cinema.domain.movie.Movie
import com.cinema.domain.movie.showtimes.MovieSchedule
import com.cinema.domain.movie.showtimes.MovieSchedules
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
import java.time.DayOfWeek
import java.time.LocalTime

class SaveMovieShowtimeRoute : KoinTest {

    @Test
    fun `can save movie showtime`() {
        withTestApplication(Application::modulesForTest) {
            val movie = givenMovie()
            with(
                handleRequest(HttpMethod.Post, "/movies/${movie.imdbId}/showtime") {
                    addHeader(HttpHeaders.ContentType, ContentType.Application.Json.toString())
                    setBody(Json.encodeToString(anySaveShowtimeRequest()))
                }
            ) {
                Assertions.assertEquals(HttpStatusCode.Created, response.status())
            }
        }
    }

    @Test
    fun `returns not found if movie does not exist`() {
        withTestApplication(Application::modulesForTest) {
            with(
                handleRequest(HttpMethod.Post, "/movies/${anyString()}/showtime") {
                    addHeader(HttpHeaders.ContentType, ContentType.Application.Json.toString())
                    setBody(Json.encodeToString(anySaveShowtimeRequest()))
                }
            ) {
                Assertions.assertEquals(HttpStatusCode.NotFound, response.status())
            }
        }
    }

    @Test
    fun `cannot save movie showtime two times`() {
        withTestApplication(Application::modulesForTest) {
            val movie = givenMovie()
            val showtime = givenMovieShowTime(movie)
            with(
                handleRequest(HttpMethod.Post, "/movies/${movie.imdbId}/showtime") {
                    addHeader(HttpHeaders.ContentType, ContentType.Application.Json.toString())
                    setBody(Json.encodeToString(anySaveShowtimeRequest(dayOfWeek = showtime.dayOfWeek, time = showtime.startAt)))
                }
            ) {
                Assertions.assertEquals(HttpStatusCode.Conflict, response.status())
            }
        }
    }

    private fun givenMovie() = anyMovie().apply {
        val movies: InMemoryMovies by inject()
        movies.save(this)
    }

    private fun anySaveShowtimeRequest(dayOfWeek: DayOfWeek = DayOfWeek.MONDAY, time: LocalTime = LocalTime.now()) =
        SaveMovieShowtimeHandler.Request(dayOfWeek, time, BigDecimal.TEN)

    private fun givenMovieShowTime(movie: Movie): Showtime {
        val schedules: MovieSchedules by inject()
        val showtime = anyShowtime()
        val schedule = MovieSchedule.scheduleFor(movie).apply { this.addShowtime(showtime) }
        schedules.save(schedule)
        return showtime
    }

}

