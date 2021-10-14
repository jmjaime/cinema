package com.cinema.infrastructure.rest.routes

import io.ktor.application.*
import io.ktor.http.content.*
import io.ktor.response.*
import io.ktor.routing.*

fun Application.swaggerRoutes() {
    routing {
        static("/static") {
            resources("files")
        }

        get("/") {
            call.respondRedirect("/swagger-ui/index.html?url=/static/test.json", true)
        }
    }
}