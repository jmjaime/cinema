package com.cinema.infrastructure.persistence.remote

import com.cinema.anyString
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.features.json.*
import io.ktor.client.features.json.serializer.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockserver.client.MockServerClient
import org.mockserver.junit.jupiter.MockServerExtension
import org.mockserver.model.HttpRequest
import org.mockserver.model.HttpResponse
import org.mockserver.model.MediaType
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

@ExtendWith(MockServerExtension::class)
class OMDbClientTest(private val server: MockServerClient) {

    companion object {
        private val apiKey = anyString()
    }

    private val url = "http://localhost:${server.port}"
    private lateinit var omdbClient: OMDbClient


    @BeforeEach
    fun setUp() {
        server.reset()
        omdbClient = OMDbClient(createHttpClient(), url, apiKey)
    }

    private fun createHttpClient() = HttpClient(CIO) {
        install(JsonFeature) {
            serializer = KotlinxSerializer(kotlinx.serialization.json.Json {
                ignoreUnknownKeys = true
            })
        }
    }

    @Test
    fun `can get movie by id`() {
        val movieId = anyString()
        val response = givenResponse(movieId)
        givenARequest(movieId, response)

        val result = omdbClient.getMovie(movieId)

        assertNotNull(result)
        assertEquals(movieId, result.imdbID)
    }

    private fun givenResponse(movieId: String) =
        """{
            "Title":"The Fast and the Furious",
            "Year":"2001",
            "Rated":"PG-13",
            "Released":"22 Jun 2001",
            "Runtime":"106 min",
            "Genre":"Action, Crime, Thriller",
            "Director":"Rob Cohen",
            "Writer":"Ken Li (magazine article \"Racer X\"), Gary Scott Thompson (screen story), Gary Scott Thompson (screenplay), Erik Bergquist (screenplay), David Ayer (screenplay)",
            "Actors":"Paul Walker, Vin Diesel, Michelle Rodriguez, Jordana Brewster",
            "Plot":"Los Angeles police officer Brian O'Conner must decide where his loyalty really lies when he becomes enamored with the street racing world he has been sent undercover to destroy.",
            "Language":"English, Spanish",
            "Country":"USA, Germany",
            "Awards":"11 wins & 12 nominations.",
            "Poster":"https://m.media-amazon.com/images/M/MV5BNzlkNzVjMDMtOTdhZC00MGE1LTkxODctMzFmMjkwZmMxZjFhXkEyXkFqcGdeQXVyNjU0OTQ0OTY@._V1_SX300.jpg",
            "Ratings":[
            {
                "Source":"Internet Movie Database",
                "Value":"6.8/10"
            },
            {
                "Source":"Rotten Tomatoes",
                "Value":"53%"
            },
            {
                "Source":"Metacritic",
                "Value":"58/100"
            }
            ],
            "Metascore":"58",
            "imdbRating":"6.8",
            "imdbVotes":"322,264",
            "imdbID":"$movieId",
            "Type":"movie",
            "DVD":"01 Jan 2002",
            "BoxOffice":"$142,542,950",
            "Production":"Universal Pictures",
            "Website":"http://www.thefastandthefurious.com",
            "Response":"True"
    }"""

    private fun givenARequest(movieId: String, response: String) {
        server.`when`(
            HttpRequest.request()
                .withMethod("GET")
                .withPath("/")
                .withQueryStringParameter("i", movieId)
                .withQueryStringParameter("apikey", apiKey)
        ).respond(
            HttpResponse.response()
                .withStatusCode(200)
                .withContentType(MediaType.APPLICATION_JSON)
                .withBody(response)
        )
    }


}