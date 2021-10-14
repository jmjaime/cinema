package com.cinema.infrastructure.rest.handlers.cinema

import com.cinema.domain.actions.RateMovie
import com.cinema.domain.rating.Rating
import kotlinx.serialization.Serializable

class RateMovieHandler(private val rateMovie: RateMovie) {

    operator fun invoke(movieId: String, request: Request) {
        rateMovie(
            RateMovie.Request(
                movieId = movieId,
                customer = request.user,
                rating = ratingFrom(request.rate),
                comment = request.comment
            )
        )
    }

    private fun ratingFrom(rate: Int): Rating = if (rate in 1..5) Rating.values()[rate] else throw RuntimeException()

    @Serializable
    data class Request(val user: String, val rate: Int, val comment: String?)
}