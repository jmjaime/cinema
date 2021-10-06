package com.cinema.domain.movie.showtimes

import com.cinema.domain.errors.ShowTimeAlreadyExists
import com.cinema.domain.movie.Movie


data class MovieSchedule(val movieId: String, private val showtimes: MutableList<Showtime>) {
    companion object {
        fun scheduleFor(movie: Movie) = MovieSchedule(movie.imdbId, mutableListOf())
    }

    init {
        check(movieId.isNotBlank()) { "movieId is mandatory" }
    }

    fun showTimes(): List<Showtime> = showtimes.toList()

    fun addShowTime(showtime: Showtime) {
        showtimes.firstOrNull { it.startAt == showtime.startAt }
            ?.let { throw ShowTimeAlreadyExists(this.movieId, showtime.startAt) }
        showtimes.add(showtime)
    }
}
