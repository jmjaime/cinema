package com.cinema.domain.movie.showtimes

interface MovieSchedules {
    fun findById(movieId: String): MovieSchedule?

    fun save(movieSchedule: MovieSchedule)

}