package com.cinema

import com.cinema.domain.movie.IMDBRating
import com.cinema.domain.movie.Movie
import com.cinema.domain.movie.showtimes.Showtime
import java.math.BigDecimal
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalTime
import java.util.*

fun anyString() = UUID.randomUUID().toString()

fun anyMovie(id: String = anyString()) = Movie(
    imdbId = id,
    name = anyString(),
    releaseDate = LocalDate.now(),
    imdbRating = anyRating(),
    runtime = anyString()
)

fun anyRating() = IMDBRating(rating = BigDecimal.ONE, votes = 10)

fun anyShowtime() = Showtime(dayOfWeek = DayOfWeek.FRIDAY, startAt = LocalTime.now(), price = BigDecimal.ONE)
