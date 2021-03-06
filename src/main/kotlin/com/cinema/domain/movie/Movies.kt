package com.cinema.domain.movie

interface Movies {

    fun findById(id: String): Movie?

    fun findAll(): List<Movie>
    fun findByIdIn(ids: List<String>): List<Movie>

}
