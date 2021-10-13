package com.cinema.domain.actions

import com.cinema.domain.movie.MovieLocator
import com.cinema.domain.movie.Price
import com.cinema.domain.movie.showtimes.DailyShowtime
import com.cinema.domain.movie.showtimes.DailyShowtimes
import com.cinema.domain.movie.showtimes.Showtime
import java.time.DayOfWeek
import java.time.LocalTime

class SaveMovieShowtime(
    private val movieLocator: MovieLocator,
    private val dailyShowtimes: DailyShowtimes
) {

    operator fun invoke(request: Request): DailyShowtime {
        val dailyShowtime = findDailyShowtime(request)
        val showtimes = showtimeFrom(request)
        val updatedDailyShowtime = dailyShowtime.copy(showtimes = showtimes)
        dailyShowtimes.save(updatedDailyShowtime)
        return updatedDailyShowtime
    }

    private fun findDailyShowtime(request: Request): DailyShowtime {
        val movie = movieLocator.findMovie(request.movieId)
        return dailyShowtimes.findByMovieIdAndDay(movie.imdbId, request.dayOfWeek) ?: DailyShowtime.dailyShowtimeFor(
            movie = movie,
            request.dayOfWeek
        )
    }

    private fun showtimeFrom(request: Request) = request.showTimes.map {
        Showtime(startAt = it.first, price = it.second)
    }

    data class Request(val movieId: String, val dayOfWeek: DayOfWeek, val showTimes: List<Pair<LocalTime, Price>>)

}