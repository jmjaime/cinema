package com.cinema.infrastructure.rest.handlers

import com.cinema.domain.actions.FetchMovieDetails
import com.cinema.infrastructure.rest.models.MovieDetailsModel
import com.cinema.infrastructure.rest.models.MovieModel
import com.cinema.infrastructure.rest.models.toDetailsModel
import com.cinema.infrastructure.rest.models.toModel

class FetchMovieDetailsHandler(private val fetchMovieDetails: FetchMovieDetails) {

    operator fun invoke(movieId:String): MovieDetailsModel {
       return fetchMovieDetails.invoke(FetchMovieDetails.Request(movieId)).toDetailsModel()
    }
}