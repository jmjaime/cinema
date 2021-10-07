package com.cinema.domain.actions

import com.cinema.domain.errors.MovieNotFound
import com.cinema.domain.movie.Movies
import com.cinema.domain.movie.showtimes.MovieSchedule
import com.cinema.domain.movie.showtimes.MovieSchedules
import com.cinema.domain.movie.showtimes.Showtime
import java.math.BigDecimal
import java.time.DayOfWeek
import java.time.LocalTime

class SaveMovieShowTime(
    private val movies: Movies,
    private val movieSchedules: MovieSchedules
) {

    operator fun invoke(request: Request): Showtime {
        val schedule = findSchedule(request)
        val showtime = showtimeFrom(request)
        schedule.addShowTime(showtime)
        movieSchedules.save(schedule)
        return showtime
    }

    private fun findSchedule(request: Request): MovieSchedule {
        val movie = movies.findById(request.movieId) ?: throw MovieNotFound(movieId = request.movieId)
        return movieSchedules.findById(movie.imdbId) ?: MovieSchedule.scheduleFor(movie = movie)
    }

    private fun showtimeFrom(request: Request) =
        Showtime(dayOfWeek = request.dayOfWeek, startAt = request.startAt, price = request.price)

    data class Request(val movieId: String, val dayOfWeek: DayOfWeek, val startAt: LocalTime, val price: BigDecimal)
}