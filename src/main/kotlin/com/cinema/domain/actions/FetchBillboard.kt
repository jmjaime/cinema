package com.cinema.domain.actions

import com.cinema.domain.billboard.Billboard
import com.cinema.domain.movie.Movies
import com.cinema.domain.movie.showtimes.DailyShowtimes

class FetchBillboard(
    private val dailyShowtimes: DailyShowtimes,
    private val movies: Movies
) {

    operator fun invoke(): Billboard {
        val moviesWithProjections = dailyShowtimes.findAll().filter { it.showtimes.isNotEmpty() }
        val availableMovies = movies.findByIdIn(moviesWithProjections.map { it.movieId })
        return Billboard(availableMovies)
    }

}