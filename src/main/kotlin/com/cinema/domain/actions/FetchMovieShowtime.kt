package com.cinema.domain.actions

import com.cinema.domain.movie.MovieLocator
import com.cinema.domain.movie.showtimes.DailyShowtime
import com.cinema.domain.movie.showtimes.DailyShowtimes
import java.time.DayOfWeek

class FetchMovieShowtime(
    private val movieLocator: MovieLocator,
    private val dailyShowtimes: DailyShowtimes
) {

    operator fun invoke(request: Request): DailyShowtime {
        val movie = movieLocator.findMovie(request.movieId)
        return dailyShowtimes.findByMovieIdAndDay(movie.imdbId, request.dayOfWeek) ?: DailyShowtime.dailyShowtimeFor(
            movie = movie,
            request.dayOfWeek
        )
    }

    data class Request(val movieId: String, val dayOfWeek: DayOfWeek)
}