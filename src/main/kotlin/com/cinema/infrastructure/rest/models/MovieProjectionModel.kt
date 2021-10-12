package com.cinema.infrastructure.rest.models

import com.cinema.domain.movie.showtimes.MovieProjection
import com.cinema.infrastructure.rest.serializer.BigDecimalSerializer
import com.cinema.infrastructure.rest.serializer.LocalDateTimeSerializer
import kotlinx.serialization.Serializable
import java.math.BigDecimal
import java.time.LocalDateTime

@Serializable
data class MovieProjectionModel(
    @Serializable(with = LocalDateTimeSerializer::class)
    val startAt: LocalDateTime,
    @Serializable(with = BigDecimalSerializer::class)
    val price: BigDecimal
)

fun MovieProjection.toModel() = MovieProjectionModel(startAt = this.startAt, price = this.price.amount)