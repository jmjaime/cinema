package com.cinema.infrastructure.rest.routes

import com.cinema.infrastructure.rest.handlers.FetchMoviesHandler
import com.cinema.infrastructure.rest.handlers.SaveMovieShowtimeHandler
import io.ktor.application.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import org.koin.ktor.ext.inject

fun Application.backofficeRoutes() {

    val fetchMoviesHandler: FetchMoviesHandler by inject()
    val saveMovieShowtimeHandler: SaveMovieShowtimeHandler by inject()

    routing {
        get("/movies") {
            call.respond(fetchMoviesHandler())
        }
        put("movies/{id}/showtime/{day}") {
            val movieId = call.parameters["id"]!!
            val day = call.parameters["day"]!!
            val body = call.receive<SaveMovieShowtimeHandler.Request>()
            call.respond(saveMovieShowtimeHandler(movieId, day, body))
        }
    }
}
