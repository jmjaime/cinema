package com.cinema.domain.actions

import com.cinema.domain.errors.MovieNotFound
import com.cinema.domain.movie.Movie
import com.cinema.domain.movie.Movies

class FetchMovieDetails(private val movies: Movies) {

    operator fun invoke(request: Request): Movie {
        return movies.findById(request.id) ?: throw MovieNotFound(request.id)
    }

    data class Request(val id: String)
}