package com.cinema.domain.actions

import com.cinema.domain.billboard.Billboard
import com.cinema.domain.movie.Movies
import com.cinema.domain.movie.showtimes.DailyShowtimes

class FetchBillboard(
    private val dailyShowtimes: DailyShowtimes,
    private val movies: Movies
) {

    operator fun invoke(): Billboard {
        val availableMovies = movies.findAll().filter { movie ->
            dailyShowtimes.findByMovieId(movie.imdbId).any { dailyShowtime ->
                dailyShowtime.showtimes.isNotEmpty()
            }
        }
        return Billboard(availableMovies)
    }

}