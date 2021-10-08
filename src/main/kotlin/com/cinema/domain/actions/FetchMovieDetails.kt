package com.cinema.domain.actions

import com.cinema.domain.movie.Movie
import com.cinema.domain.movie.MovieLocator

class FetchMovieDetails(private val movieLocator: MovieLocator) {

    operator fun invoke(request: Request): Movie {
        return movieLocator.findMovie(request.id)
    }

    data class Request(val id: String)
}