package com.cinema.infrastructure.rest.handlers

import com.cinema.domain.actions.FetchMovieTimes
import com.cinema.infrastructure.rest.models.MovieProjectionModel
import com.cinema.infrastructure.rest.models.toModel

class FetchMovieTimesHandler(private val fetchMovieTimes: FetchMovieTimes) {

    operator fun invoke(movieId:String): List<MovieProjectionModel> {
        return fetchMovieTimes(FetchMovieTimes.Request(movieId)).map { it.toModel() }
    }
}