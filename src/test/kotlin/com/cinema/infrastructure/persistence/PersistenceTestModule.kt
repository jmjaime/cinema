package com.cinema.infrastructure.persistence

import com.cinema.domain.movie.Movies
import com.cinema.domain.movie.showtimes.DailyShowtimes
import com.cinema.domain.rating.CustomerVotes
import com.cinema.infrastructure.persistence.memory.InMemoryCustomerVotes
import com.cinema.infrastructure.persistence.memory.InMemoryDailyShowtimes
import com.cinema.infrastructure.persistence.memory.InMemoryMovies
import org.koin.dsl.bind
import org.koin.dsl.module

val persistenceTestModule = module {
    single { InMemoryCustomerVotes() } bind CustomerVotes::class
    single { InMemoryDailyShowtimes() } bind DailyShowtimes::class
    single { InMemoryMovies() } bind Movies::class

}