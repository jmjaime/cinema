package com.cinema.infrastructure.rest.models

import com.cinema.domain.movie.showtimes.DailyShowtime
import kotlinx.serialization.Serializable

@Serializable
data class DailyShowtimeModel(
 val movieId:String,
 val day:String,
 val showtimes:List<ShowtimeModel>
)

fun DailyShowtime.toModel() = DailyShowtimeModel(
    movieId= movieId,
    day=day.name,
    showtimes = showtimes.map { it.toModel() }
)