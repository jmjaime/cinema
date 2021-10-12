package com.cinema.domain.config

import com.cinema.domain.actions.*
import org.koin.dsl.module


val actionsModule = module {
    single { FetchBillboard(get(), get()) }
    single { FetchMovieDetails(get()) }
    single { FetchMovies(get()) }
    single { FetchMovieTimes(get(), get(), get()) }
    single { RateMovie(get(), get()) }
    single { SaveMovieShowTime(get(), get()) }
}