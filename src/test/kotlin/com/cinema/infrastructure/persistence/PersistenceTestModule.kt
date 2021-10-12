package com.cinema.infrastructure.persistence

import com.cinema.domain.movie.Movies
import com.cinema.domain.movie.showtimes.MovieSchedules
import com.cinema.domain.rating.CustomerVotes
import com.cinema.infrastructure.persistence.memory.InMemoryCustomerVotes
import com.cinema.infrastructure.persistence.memory.InMemoryMovieSchedules
import com.cinema.infrastructure.persistence.memory.InMemoryMovies
import org.koin.dsl.bind
import org.koin.dsl.module

val persistenceTestModule = module {
    single { InMemoryCustomerVotes() } bind CustomerVotes::class
    single { InMemoryMovieSchedules() } bind MovieSchedules::class
    single { InMemoryMovies() } bind Movies::class

}