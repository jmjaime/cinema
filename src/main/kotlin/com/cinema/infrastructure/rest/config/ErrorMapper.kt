package com.cinema.infrastructure.rest.config

import com.cinema.domain.errors.MovieAlreadyRated
import com.cinema.domain.errors.MovieNotFound
import com.cinema.infrastructure.rest.errors.InvalidDayOfWeek
import io.ktor.application.*
import io.ktor.features.*
import io.ktor.http.*
import io.ktor.response.*
import kotlinx.serialization.SerializationException

fun Application.errorMapper() {
    install(StatusPages) {
        exception<MovieNotFound> {
            call.respond(HttpStatusCode.NotFound)
        }
        exception<MovieAlreadyRated> {
            call.respond(HttpStatusCode.Conflict)
        }
        exception<SerializationException> {
            call.respond(HttpStatusCode.BadRequest)
        }
        exception<InvalidDayOfWeek> {
            call.respond(HttpStatusCode.NotFound)
        }
    }
}