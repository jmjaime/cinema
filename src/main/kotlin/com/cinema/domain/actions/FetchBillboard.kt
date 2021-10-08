package com.cinema.domain.actions

import com.cinema.domain.billboard.Billboard
import com.cinema.domain.movie.Movies
import com.cinema.domain.movie.showtimes.MovieSchedules

class FetchBillboard(
    private val movieSchedules: MovieSchedules,
    private val movies: Movies
) {

    operator fun invoke(): Billboard {
        val movieSchedulesWithProjections = movieSchedules.findAll().filter { it.showtimes().isNotEmpty() }
        val availableMovies = movies.findByIdIn(movieSchedulesWithProjections.map { it.movieId })
        return Billboard(availableMovies)
    }

}