package com.cinema.domain.movie

import java.time.LocalDate

data class Movie(
    val imdbId: String,
    val name: String,
    val releaseDate: LocalDate,
    val imdbRating: IMDBRating,
    val runtime: String
) {
    init {
        check(imdbId.isNotBlank()) { "imdbId is mandatory" }
        check(name.isNotBlank()) { "name is mandatory" }
        check(runtime.isNotBlank()) { "runtime is mandatory" }
    }
}