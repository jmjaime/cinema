package com.cinema.domain.actions

import com.cinema.domain.movie.MovieLocator
import com.cinema.domain.movie.Price
import com.cinema.domain.movie.showtimes.MovieSchedule
import com.cinema.domain.movie.showtimes.MovieSchedules
import com.cinema.domain.movie.showtimes.Showtime
import java.time.DayOfWeek
import java.time.LocalTime

class SaveMovieShowtime(
    private val movieLocator: MovieLocator,
    private val movieSchedules: MovieSchedules
) {

    operator fun invoke(request: Request): Showtime {
        val schedule = findSchedule(request)
        val showtime = showtimeFrom(request)
        schedule.addShowtime(showtime)
        movieSchedules.save(schedule)
        return showtime
    }

    private fun findSchedule(request: Request): MovieSchedule {
        val movie = movieLocator.findMovie(request.movieId)
        return movieSchedules.findById(movie.imdbId) ?: MovieSchedule.scheduleFor(movie = movie)
    }

    private fun showtimeFrom(request: Request) =
        Showtime(dayOfWeek = request.dayOfWeek, startAt = request.startAt, price = request.price)

    data class Request(val movieId: String, val dayOfWeek: DayOfWeek, val startAt: LocalTime, val price: Price)
}