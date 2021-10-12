package com.cinema.infrastructure.rest.routes

import com.cinema.infrastructure.rest.handlers.FetchMoviesHandler
import com.cinema.infrastructure.rest.handlers.SaveMovieShowtimeHandler
import io.ktor.application.*
import io.ktor.http.*
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
        post("movies/{id}/showtime") {
            val movieId = call.parameters["id"]!!
            val body = call.receive<SaveMovieShowtimeHandler.Request>()
            saveMovieShowtimeHandler(movieId, body)
            call.respond(HttpStatusCode.Created)
        }
    }
}
