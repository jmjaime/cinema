package com.cinema.infrastructure.rest.models

import com.cinema.domain.billboard.Billboard
import com.cinema.domain.movie.Movie
import kotlinx.serialization.Serializable

@Serializable
data class BillboardModel(val availableMovies: List<BillboardMovieDescriptionModel>)

@Serializable
data class BillboardMovieDescriptionModel(
    val name: String,
    val description: String,
    val runtime: String,
    val actors: String,
    val poster: String?
)

fun Billboard.toModel() = BillboardModel(
    availableMovies = this.availableMovies.map { it.toBillboardMovieDescriptionModel() }
)

fun Movie.toBillboardMovieDescriptionModel() = BillboardMovieDescriptionModel(
    name = name,
    description = description,
    runtime = runtime,
    poster = poster,
    actors = actors
)