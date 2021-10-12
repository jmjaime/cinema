package com.cinema.infrastructure.rest.routes

import com.cinema.infrastructure.rest.handlers.FetchBillboardHandler
import com.cinema.infrastructure.rest.handlers.FetchMovieDetailsHandler
import com.cinema.infrastructure.rest.handlers.FetchMovieTimesHandler
import io.ktor.application.*
import io.ktor.response.*
import io.ktor.routing.*
import org.koin.ktor.ext.inject

fun Application.cinemaRoutes() {

    val fetchMovieDetailsHandler: FetchMovieDetailsHandler by inject()
    val fetchBillboardHandler: FetchBillboardHandler by inject()
    val fetchMovieTimesHandler: FetchMovieTimesHandler by inject()


    routing {
        get("/billboard") {
            call.respond(fetchBillboardHandler())
        }

        get("/movies/{id}") {
            call.respond(fetchMovieDetailsHandler(call.parameters["id"]!!))
        }
        get("/movies/{id}/times") {
            call.respond(fetchMovieTimesHandler(call.parameters["id"]!!))

        }
        post("/movies/{id}/rate") {


        }
    }
}