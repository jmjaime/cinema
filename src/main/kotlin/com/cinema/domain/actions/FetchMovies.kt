package com.cinema.domain.actions

import com.cinema.domain.movie.Movie
import com.cinema.domain.movie.Movies

class FetchMovies(private val movies: Movies) {

    operator fun invoke(): List<Movie> {
        return movies.findAll()
    }

}