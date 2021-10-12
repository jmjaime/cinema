package com.cinema.domain.movie

import com.cinema.anyString
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class MovieTest {

    @Test
    fun `can create valid movie`() {
        givenMovie(
            imdbId = "imdbId",
            name = "name",
            runtime = "120 min"
        )
    }

    @Test
    fun `cannot create movie with invalid imdbId`() {
        val error = assertThrows<IllegalStateException> { givenMovie(imdbId = "") }
        assertEquals("imdbId is mandatory", error.message)
    }

    @Test
    fun `cannot create movie with invalid name`() {
        val error = assertThrows<IllegalStateException> { givenMovie(name = "") }
        assertEquals("name is mandatory", error.message)

    }

    @Test
    fun `cannot create movie with invalid runtime`() {
        val error = assertThrows<IllegalStateException> { givenMovie(runtime = "") }
        assertEquals("runtime is mandatory", error.message)
    }

    private fun givenMovie(
        imdbId: String = anyString(),
        name: String = anyString(),
        runtime: String = anyString()
    ) = Movie(
        imdbId = imdbId,
        name = name,
        releaseDate = "3 Jun 1999",
        imdbRating = IMDBRating(rating = "4.5", votes = "109"),
        runtime = runtime
    )
}