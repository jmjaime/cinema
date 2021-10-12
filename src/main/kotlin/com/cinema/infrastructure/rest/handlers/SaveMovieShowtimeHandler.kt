package com.cinema.infrastructure.rest.handlers

import com.cinema.domain.actions.SaveMovieShowtime
import com.cinema.domain.movie.Price
import com.cinema.infrastructure.rest.serializer.BigDecimalSerializer
import com.cinema.infrastructure.rest.serializer.LocalTimeSerializer
import kotlinx.serialization.Serializable
import java.math.BigDecimal
import java.time.DayOfWeek
import java.time.LocalTime

class SaveMovieShowtimeHandler(private val saveMovieShowtime: SaveMovieShowtime) {

    operator fun invoke(movieId: String, request: Request) {
        saveMovieShowtime(
            SaveMovieShowtime.Request(
                movieId = movieId,
                dayOfWeek = request.dayOfWeek,
                startAt = request.time,
                price = Price(request.price)
            )
        )
    }

    @Serializable
    data class Request(
        val dayOfWeek: DayOfWeek,
        @Serializable(with = LocalTimeSerializer::class)
        val time: LocalTime,
        @Serializable(with = BigDecimalSerializer::class)
        val price: BigDecimal
        )
}
