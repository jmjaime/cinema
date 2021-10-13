package com.cinema.infrastructure.rest.handlers

import com.cinema.domain.actions.SaveMovieShowtime
import com.cinema.domain.movie.Price
import com.cinema.infrastructure.rest.models.ShowtimeModel
import com.cinema.infrastructure.rest.models.toModel
import com.cinema.infrastructure.rest.serializer.BigDecimalSerializer
import com.cinema.infrastructure.rest.serializer.LocalTimeSerializer
import kotlinx.serialization.Serializable
import java.math.BigDecimal
import java.time.DayOfWeek
import java.time.LocalTime

class SaveMovieShowtimeHandler(private val saveMovieShowtime: SaveMovieShowtime) {

    operator fun invoke(movieId: String, dayOfWeek: String, request: Request): List<ShowtimeModel> {
        return saveMovieShowtime(
            SaveMovieShowtime.Request(
                movieId = movieId,
                dayOfWeek = DayOfWeek.valueOf(dayOfWeek.uppercase()),
                showTimes = request.showTimeRequests.map { it.startAt to Price(it.price) }
            )
        ).showtimes.map { it.toModel() }
    }

    @Serializable
    data class Request(
        val showTimeRequests: List<ShowTimeRequest>
    )

    @Serializable
    data class ShowTimeRequest(
        @Serializable(with = LocalTimeSerializer::class)
        val startAt: LocalTime,
        @Serializable(with = BigDecimalSerializer::class)
        val price: BigDecimal
    )
}
