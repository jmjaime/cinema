package com.cinema

import com.cinema.domain.movie.IMDBRating
import com.cinema.domain.movie.Movie
import com.cinema.domain.movie.Price
import com.cinema.domain.movie.showtimes.MovieSchedule
import com.cinema.domain.movie.showtimes.Showtime
import com.cinema.domain.rating.CustomerVote
import com.cinema.domain.rating.Rating
import com.cinema.infrastructure.persistence.memory.InMemoryMovies
import java.math.BigDecimal
import java.time.DayOfWeek
import java.time.LocalTime
import java.util.*

fun anyString() = UUID.randomUUID().toString()

fun anyMovie(id: String = anyString()) = Movie(
    imdbId = id,
    name = anyString(),
    releaseDate = "22 Jun 2001",
    imdbRating = anyRating(),
    runtime = anyString()
)

fun givenPersistedMovie(movies: InMemoryMovies, movie: Movie = anyMovie()) = movie.also { movies.save(it) }

fun anyRating() = IMDBRating(rating = "6.2", votes = "10096")

fun anyShowtime() = Showtime(dayOfWeek = DayOfWeek.FRIDAY, startAt = LocalTime.now(), price = Price(BigDecimal.ONE))

fun anyMovieSchedule(movieId: String) = MovieSchedule(movieId = movieId, showtimes = mutableListOf(anyShowtime()))

fun anyCustomerVote(customer: String = anyString(), movieId: String = anyString()) = CustomerVote(
    customer = customer,
    movieId = movieId,
    rating = Rating.FOUR,
    comment = anyString()
)
