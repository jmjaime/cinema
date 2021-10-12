package com.cinema.infrastructure.persistence.memory

import com.cinema.domain.movie.showtimes.MovieSchedule
import com.cinema.domain.movie.showtimes.MovieSchedules

class InMemoryMovieSchedules(
    private val schedules: MutableMap<String, MovieSchedule> = mutableMapOf()
) : MovieSchedules {

    override fun findById(movieId: String) = schedules[movieId]

    override fun save(movieSchedule: MovieSchedule) {
        schedules[movieSchedule.movieId] = movieSchedule
    }

    override fun findAll() = schedules.values.toList()
}