package com.cinema.domain.actions

import com.cinema.domain.errors.MovieNotFound
import com.cinema.domain.movie.Movies
import com.cinema.domain.movie.showtimes.MovieSchedules
import com.cinema.domain.movie.showtimes.Showtime

class FetchMovieTimes(
    private val movies: Movies,
    private val movieSchedules: MovieSchedules
) {

    operator fun invoke(request: Request): List<Showtime> {
        val movie = movies.findById(request.movieId) ?: throw MovieNotFound(movieId = request.movieId)
        return movieSchedules.findById(movie.imdbId)?.showTimes() ?: emptyList()
    }

    data class Request(val movieId: String)

}