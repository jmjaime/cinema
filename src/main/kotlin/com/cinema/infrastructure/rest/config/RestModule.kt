package com.cinema.infrastructure.rest

import com.cinema.infrastructure.rest.handlers.FetchBillboardHandler
import com.cinema.infrastructure.rest.handlers.FetchMovieDetailsHandler
import com.cinema.infrastructure.rest.handlers.FetchMovieTimesHandler
import com.cinema.infrastructure.rest.handlers.FetchMoviesHandler
import org.koin.dsl.module

val restModule = module {
    single { FetchMoviesHandler(get()) }
    single { FetchMovieDetailsHandler(get()) }
    single { FetchBillboardHandler(get()) }
    single { FetchMovieTimesHandler(get()) }
}