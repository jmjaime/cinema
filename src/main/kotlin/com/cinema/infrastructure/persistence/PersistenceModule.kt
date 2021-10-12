package com.cinema.infrastructure.persistence

import com.cinema.domain.movie.Movies
import com.cinema.domain.movie.showtimes.MovieSchedules
import com.cinema.domain.rating.CustomerVotes
import com.cinema.infrastructure.persistence.memory.InMemoryCustomerVotes
import com.cinema.infrastructure.persistence.memory.InMemoryMovieSchedules
import com.cinema.infrastructure.persistence.remote.OMDbClient
import com.cinema.infrastructure.persistence.remote.OMDbMovies
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.features.json.*
import io.ktor.client.features.json.serializer.*
import org.koin.dsl.module

val persistenceModule = module {
    single<CustomerVotes> { InMemoryCustomerVotes() }
    single<MovieSchedules> { InMemoryMovieSchedules() }
}

val remotePersistenceModule = module {
    single<Movies> {
        OMDbMovies(
            availableMovies = System.getenv("movies").split(","),
            cacheTimeOutInSeconds = System.getenv("movie_cache_timeout").toLong(),
            omdbClient = get()
        )
    }
    single {
        OMDbClient(
            httpClient = get(),
            urlOMDb = System.getenv("omdb_url"),
            apiKey = System.getenv("api_key")
        )
    }

    single {
        HttpClient(CIO) {
            install(JsonFeature) {
                serializer = KotlinxSerializer(kotlinx.serialization.json.Json {
                    ignoreUnknownKeys = true
                })
            }
        }
    }
}