package com.cinema.infrastructure.rest.models

import com.cinema.domain.movie.Movie
import kotlinx.serialization.Serializable

@Serializable
data class MovieDetailsModel(
    val id: String,
    val name: String,
    val description: String,
    val releaseDate: String,
    val runtime: String,
    val imdbRating: String,
)

fun Movie.toDetailsModel() = MovieDetailsModel(
    id = imdbId,
    name = name,
    description = description,
    releaseDate = releaseDate,
    runtime = runtime,
    imdbRating = imdbRating.rating
)