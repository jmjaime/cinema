package com.cinema.domain.movie

import com.cinema.domain.errors.MovieNotFound

class MovieLocator(private val movies: Movies) {

    fun findMovie(movieId: String): Movie {
        return movies.findById(movieId) ?: throw MovieNotFound(movieId)
    }
}