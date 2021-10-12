package com.cinema

import com.cinema.domain.config.actionsModule
import com.cinema.domain.config.moviesModule
import com.cinema.infrastructure.persistence.persistenceModule
import com.cinema.infrastructure.rest.errorMapper
import com.cinema.infrastructure.rest.config.restModule
import com.cinema.infrastructure.rest.routes.backofficeRoutes
import com.cinema.infrastructure.rest.routes.cinemaRoutes
import io.ktor.application.*
import io.ktor.features.*
import io.ktor.serialization.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import kotlinx.serialization.json.Json
import org.koin.ktor.ext.Koin
import org.koin.logger.SLF4JLogger

fun Application.main() {
    koinModules()
    routes()
}


fun Application.koinModules() {
    install(CallLogging)
    install(Koin) {
        SLF4JLogger()
        modules(moviesModule, actionsModule, restModule, persistenceModule)
    }
}

fun Application.routes() {
    install(DefaultHeaders)
    install(ContentNegotiation) {
        json(
            Json {
                ignoreUnknownKeys = true
            }
        )
    }
    cinemaRoutes()
    backofficeRoutes()
    errorMapper()
}

fun main(args: Array<String>) {
    embeddedServer(Netty, commandLineEnvironment(args)).start()
}