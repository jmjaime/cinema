package com.cinema.domain.movie.showtimes

import java.time.DayOfWeek

interface DailyShowtimes {
    fun findByMovieId(movieId: String): List<DailyShowtime>
    fun findByMovieIdAndDay(movieId: String, day: DayOfWeek): DailyShowtime?
    fun save(dailyShowtime: DailyShowtime)

}