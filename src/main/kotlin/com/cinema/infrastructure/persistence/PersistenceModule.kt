package com.cinema.infrastructure.persistence

import com.cinema.domain.movie.IMDBRating
import com.cinema.domain.movie.Movie
import com.cinema.domain.movie.Movies
import com.cinema.domain.movie.showtimes.MovieSchedules
import com.cinema.domain.rating.CustomerVotes
import com.cinema.infrastructure.persistence.memory.InMemoryCustomerVotes
import com.cinema.infrastructure.persistence.memory.InMemoryMovieSchedules
import com.cinema.infrastructure.persistence.memory.InMemoryMovies
import org.koin.dsl.module
import java.math.BigDecimal
import java.time.LocalDate

val persistenceModule = module {
    single<CustomerVotes> { InMemoryCustomerVotes() }
    single<MovieSchedules> { InMemoryMovieSchedules() }
    single<Movies> {
        InMemoryMovies(
            movies = mutableMapOf(
                "tt0232500" to Movie(
                    imdbId = "tt0232500",
                    name ="The Fast and the Furious",
                    releaseDate = LocalDate.now(),
                    imdbRating = IMDBRating(BigDecimal.valueOf(6.8),360459),
                    runtime = "106 min"
                )
            )
        )
    }
}