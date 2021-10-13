package com.cinema.domain.actions

import com.cinema.domain.movie.MovieLocator
import com.cinema.domain.movie.showtimes.DailyShowtime
import com.cinema.domain.movie.showtimes.DailyShowtimes
import com.cinema.domain.movie.showtimes.MovieProjection
import java.time.Clock
import java.time.LocalDate
import java.time.temporal.TemporalAdjusters

class FetchMovieTimes(
    private val movieLocator: MovieLocator,
    private val dailyShowtimes: DailyShowtimes,
    private val clock: Clock
) {

    operator fun invoke(request: Request): List<MovieProjection> {
        val movie = movieLocator.findMovie(request.movieId)
        val dailyShowTimes = dailyShowtimes.findByMovieId(movie.imdbId)
        return nextMovieProjections(dailyShowTimes)
    }

    private fun nextMovieProjections(dailyShowTimes: List<DailyShowtime>): List<MovieProjection> {
        val today = LocalDate.now(clock).dayOfWeek
        val daysOfWeek = dailyShowTimes.partition { it.day < today }
        val currentWeek = daysOfWeek.second.flatMap { toMovieProjection(it) }
        val nextWeek = daysOfWeek.first.flatMap { toMovieProjection(it) }
        return currentWeek + nextWeek
    }

    private fun toMovieProjection(dailyShowtime: DailyShowtime): List<MovieProjection> {
        return dailyShowtime.showtimes.map {
            val startAt =
                LocalDate.now(clock).atTime(it.startAt).with(TemporalAdjusters.nextOrSame(dailyShowtime.day))
            MovieProjection(startAt = startAt, price = it.price)
        }

    }

    data class Request(val movieId: String)

}