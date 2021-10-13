package com.cinema.domain.movie.showtimes

import com.cinema.domain.movie.Movie
import java.time.DayOfWeek


data class DailyShowtime(val movieId: String, val day: DayOfWeek, val showtimes: List<Showtime>) {
    companion object {
        fun dailyShowtimeFor(movie: Movie, day: DayOfWeek) = DailyShowtime(movie.imdbId, day, listOf())
    }

    init {
        check(movieId.isNotBlank()) { "movieId is mandatory" }
    }

}
