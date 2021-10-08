package com.cinema.domain.actions

import com.cinema.domain.movie.MovieLocator
import com.cinema.domain.movie.showtimes.MovieProjection
import com.cinema.domain.movie.showtimes.MovieSchedules
import com.cinema.domain.movie.showtimes.Showtime
import java.time.Clock
import java.time.LocalDate
import java.time.temporal.TemporalAdjusters

class FetchMovieTimes(
    private val movieLocator: MovieLocator,
    private val movieSchedules: MovieSchedules,
    private val clock: Clock
) {

    operator fun invoke(request: Request): List<MovieProjection> {
        val movie = movieLocator.findMovie(request.movieId)
        val showTimes = movieSchedules.findById(movie.imdbId)?.showtimes() ?: emptyList()
        return nextMovieProjections(showTimes)
    }

    private fun nextMovieProjections(showtimes: List<Showtime>): List<MovieProjection> {
        val today = LocalDate.now(clock).dayOfWeek
        val daysOfWeek = showtimes.partition { it.dayOfWeek < today }
        val currentWeek = daysOfWeek.second.map { toMovieProjection(it) }
        val nextWeek = daysOfWeek.first.map { toMovieProjection(it) }
        return currentWeek + nextWeek
    }

    private fun toMovieProjection(showTime: Showtime): MovieProjection {
        val startAt =
            LocalDate.now(clock).atTime(showTime.startAt).with(TemporalAdjusters.nextOrSame(showTime.dayOfWeek))
        return MovieProjection(startAt = startAt, price = showTime.price)
    }

    data class Request(val movieId: String)

}