package com.cinema.infrastructure.persistence

import com.cinema.domain.movie.Movies
import com.cinema.domain.movie.showtimes.DailyShowtimes
import com.cinema.domain.rating.CustomerVotes
import com.cinema.infrastructure.persistence.dynamo.DynamoCustomerVotes
import com.cinema.infrastructure.persistence.dynamo.DynamoDailyShowtimes
import com.cinema.infrastructure.persistence.remote.OMDbClient
import com.cinema.infrastructure.persistence.remote.OMDbMovies
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.features.json.*
import io.ktor.client.features.json.serializer.*
import kotlinx.serialization.json.Json
import org.koin.dsl.module
import software.amazon.awssdk.auth.credentials.EnvironmentVariableCredentialsProvider
import software.amazon.awssdk.services.dynamodb.DynamoDbClient
import java.net.URI

val persistenceModule = module {
    single<DailyShowtimes> {
        DynamoDailyShowtimes(
            dynamoDbClient = get(),
            tableName = getSetting("dynamo_table"),
            mapper = Json { }
        )
    }

    single<CustomerVotes> {
        DynamoCustomerVotes(
            dynamoDbClient = get(),
            tableName = getSetting("dynamo_table")
        )
    }

    single {
        val builder = DynamoDbClient
            .builder()
            .credentialsProvider(EnvironmentVariableCredentialsProvider.create())
        getOptionalSetting("dynamodb_url")?.let { builder.endpointOverride(URI.create(it)) }
        builder.build()
    }

}

val remotePersistenceModule = module {
    single<Movies> {
        OMDbMovies(
            availableMovies = getSetting("movies").split(","),
            cacheTimeOutInSeconds = getSetting("movie_cache_timeout").toLong(),
            omdbClient = get()
        )
    }
    single {
        OMDbClient(
            httpClient = get(),
            urlOMDb = getSetting("omdb_url"),
            apiKey = getSetting("api_key")
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

fun getOptionalSetting(key: String): String? = System.getProperty(key)?.takeIf { it.isNotBlank() }
    ?: System.getenv(key)?.takeIf { it.isNotBlank() }

fun getSetting(key: String): String = getOptionalSetting(key)
    ?: throw IllegalArgumentException("setting $key is required")