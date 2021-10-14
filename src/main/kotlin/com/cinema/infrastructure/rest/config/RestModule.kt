package com.cinema.infrastructure.rest.config

import com.cinema.infrastructure.rest.handlers.*
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