package com.cinema.domain.config

import com.cinema.domain.movie.MovieLocator
import org.koin.dsl.module


val moviesModule = module {
    single { MovieLocator(get()) }
}