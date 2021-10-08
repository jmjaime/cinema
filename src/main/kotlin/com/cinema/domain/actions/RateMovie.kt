package com.cinema.domain.actions

import com.cinema.domain.errors.MovieAlreadyRated
import com.cinema.domain.errors.MovieNotFound
import com.cinema.domain.movie.Movies
import com.cinema.domain.rating.CustomerVote
import com.cinema.domain.rating.CustomerVotes
import com.cinema.domain.rating.Rating

class RateMovie(private val movies: Movies, private val customerVotes: CustomerVotes) {

    operator fun invoke(request: Request) {
        val movie = movies.findById(request.movieId) ?: throw MovieNotFound(movieId = request.movieId)
        customerVotes.findByCustomerAndMovieId(request.customer, movie.imdbId)
            ?.let { throw MovieAlreadyRated(movie.imdbId) }
        customerVotes.save(
            CustomerVote(
                customer = request.customer,
                movieId = request.movieId,
                rating = request.rating,
                comment = request.comment
            )
        )
    }

    data class Request(val customer: String, val movieId: String, val rating: Rating, val comment: String)
}