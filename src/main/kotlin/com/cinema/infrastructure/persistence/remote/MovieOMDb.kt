package com.cinema.infrastructure.persistence.remote

import kotlinx.serialization.Serializable

@Serializable
data class MovieOMDb(
    val Title: String,
    val Year: Int,
    val Rated: String,
    val Released: String,
    val Runtime: String,
    val Genre: String,
    val Director: String,
    val Actors: String,
    val Plot: String,
    val Poster: String,
    val imdbRating: String,
    val imdbVotes: String,
    val imdbID: String
)
