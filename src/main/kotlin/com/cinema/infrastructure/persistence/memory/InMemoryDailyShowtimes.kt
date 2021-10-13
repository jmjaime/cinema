package com.cinema.infrastructure.persistence.memory

import com.cinema.domain.movie.showtimes.DailyShowtime
import com.cinema.domain.movie.showtimes.DailyShowtimes
import java.time.DayOfWeek

class InMemoryDailyShowtimes(
    private val dailyShowtimes: MutableMap<String, MutableMap<DayOfWeek,DailyShowtime>> = mutableMapOf()
) : DailyShowtimes {
    override fun findByMovieId(movieId: String): List<DailyShowtime> {
        return dailyShowtimes[movieId]?.values?.toList() ?: emptyList()
    }

    override fun findByMovieIdAndDay(movieId: String, day: DayOfWeek): DailyShowtime? {
        return dailyShowtimes[movieId]?.get(day)
    }


    override fun save(dailyShowtime: DailyShowtime) {
        dailyShowtimes.getOrPut(dailyShowtime.movieId) { mutableMapOf() }[dailyShowtime.day] = dailyShowtime
    }
}