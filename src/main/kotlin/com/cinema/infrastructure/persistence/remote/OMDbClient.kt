package com.cinema.infrastructure.persistence.remote

import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.coroutines.runBlocking
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class OMDbClient(private val httpClient: HttpClient, private val urlOMDb: String, private val apiKey: String) {

    companion object {
        private val logger: Logger = LoggerFactory.getLogger(OMDbClient::class.java)
    }

    fun getMovie(id: String): MovieOMDb? {
        logger.debug("Looking for movie $id on OMDb")
        return runBlocking {
            httpClient.get<MovieOMDb>(urlOMDb) {
                header(HttpHeaders.Accept, ContentType.Application.Json.toString())
                parameter("apikey", apiKey)
                parameter("i", id)
            }
        }
    }
}