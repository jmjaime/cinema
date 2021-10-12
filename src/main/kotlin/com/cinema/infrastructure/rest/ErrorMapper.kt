package com.cinema.infrastructure.rest

import com.cinema.domain.errors.MovieNotFound
import io.ktor.application.*
import io.ktor.features.*
import io.ktor.http.*
import io.ktor.response.*

fun Application.errorMapper(){
    install(StatusPages){
        exception<MovieNotFound> {
            call.respond(HttpStatusCode.NotFound)
        }
    }
}