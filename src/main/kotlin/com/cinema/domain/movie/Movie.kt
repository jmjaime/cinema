package com.cinema.domain.movie

data class Movie(
    val imdbId: String,
    val name: String,
    val releaseDate: String,
    val imdbRating: IMDBRating,
    val runtime: String,
    val description: String,
    val actors: String,
    val poster: String?
) {
    init {
        check(imdbId.isNotBlank()) { "imdbId is mandatory" }
        check(name.isNotBlank()) { "name is mandatory" }
        check(runtime.isNotBlank()) { "runtime is mandatory" }
    }
}