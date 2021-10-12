package com.cinema.infrastructure.rest.routes

import com.cinema.infrastructure.rest.handlers.FetchMoviesHandler
import io.ktor.application.*
import io.ktor.response.*
import io.ktor.routing.*
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.koin.ktor.ext.inject

fun Application.backofficeRoutes() {

    val handler: FetchMoviesHandler by inject()

    routing {
        get("/movies") {
            call.respond(handler())
        }
        post("movies/{id}/times") {

        }
    }
}
