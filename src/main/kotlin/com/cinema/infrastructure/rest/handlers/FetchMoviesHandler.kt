package com.cinema.infrastructure.rest.handlers

import com.cinema.domain.actions.FetchMovies
import com.cinema.infrastructure.rest.models.MovieModel
import com.cinema.infrastructure.rest.models.toModel

class FetchMoviesHandler(private val fetchMovies: FetchMovies) {

    operator fun invoke(): List<MovieModel> {
        return fetchMovies.invoke().map {  it.toModel() }
    }
}