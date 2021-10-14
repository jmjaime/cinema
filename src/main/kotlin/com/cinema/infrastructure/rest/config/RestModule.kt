package com.cinema.infrastructure.rest.config

import com.cinema.infrastructure.rest.handlers.backoffice.FetchMovieShowtimeHandler
import com.cinema.infrastructure.rest.handlers.backoffice.FetchMoviesHandler
import com.cinema.infrastructure.rest.handlers.backoffice.SaveMovieShowtimeHandler
import com.cinema.infrastructure.rest.handlers.cinema.FetchBillboardHandler
import com.cinema.infrastructure.rest.handlers.cinema.FetchMovieDetailsHandler
import com.cinema.infrastructure.rest.handlers.cinema.FetchMovieTimesHandler
import com.cinema.infrastructure.rest.handlers.cinema.RateMovieHandler
import org.koin.dsl.module

val restModule = module {
    single { FetchMoviesHandler(get()) }
    single { FetchMovieDetailsHandler(get()) }
    single { FetchBillboardHandler(get()) }
    single { FetchMovieTimesHandler(get()) }
    single { RateMovieHandler(get()) }
    single { SaveMovieShowtimeHandler(get()) }
    single { FetchMovieShowtimeHandler(get()) }
}