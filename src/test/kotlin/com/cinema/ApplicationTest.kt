package com.cinema.infrastructure.routes

import com.cinema.domain.config.actionsModule
import com.cinema.domain.config.moviesModule
import com.cinema.infrastructure.persistence.persistenceTestModule
import com.cinema.infrastructure.rest.restModule
import com.cinema.routes
import io.ktor.application.*
import io.ktor.features.*
import org.koin.ktor.ext.Koin
import org.koin.logger.SLF4JLogger



fun Application.modulesForTest() {
    install(CallLogging)
    install(Koin) {
        SLF4JLogger()
        modules(moviesModule, actionsModule, restModule, persistenceTestModule)
    }
    routes()
}