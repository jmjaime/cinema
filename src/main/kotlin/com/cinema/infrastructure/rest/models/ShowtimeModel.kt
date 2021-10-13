package com.cinema.infrastructure.rest.models

import com.cinema.domain.movie.showtimes.Showtime
import com.cinema.infrastructure.rest.serializer.BigDecimalSerializer
import com.cinema.infrastructure.rest.serializer.LocalTimeSerializer
import kotlinx.serialization.Serializable
import java.math.BigDecimal
import java.time.LocalTime

@Serializable
data class ShowtimeModel(
    @Serializable(with = LocalTimeSerializer::class)
    val startAt: LocalTime,
    @Serializable(with = BigDecimalSerializer::class)
    val price: BigDecimal
)

fun Showtime.toModel() = ShowtimeModel(
    startAt = this.startAt,
    price = this.price.amount
)