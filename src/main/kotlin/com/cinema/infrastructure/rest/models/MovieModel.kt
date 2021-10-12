package com.cinema.infrastructure.rest.models

import com.cinema.domain.movie.Movie
import kotlinx.serialization.Serializable

@Serializable
data class MovieModel(
    val id: String,
    val name: String
)

fun Movie.toModel() = MovieModel(
    id = imdbId,
    name = name
)