package com.cinema.infrastructure.rest.handlers.backoffice

import com.cinema.domain.actions.FetchMovieShowtime
import com.cinema.infrastructure.rest.extensions.toDayOfWeek
import com.cinema.infrastructure.rest.models.DailyShowtimeModel
import com.cinema.infrastructure.rest.models.toModel

class FetchMovieShowtimeHandler(private val fetchMovieShowtime: FetchMovieShowtime) {

    operator fun invoke(movieId: String, day: String): DailyShowtimeModel {
        return fetchMovieShowtime(
            FetchMovieShowtime.Request(
                movieId,
                day.toDayOfWeek()
            )
        ).toModel()
    }

}
