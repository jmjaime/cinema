package com.cinema.domain.movie

class InMemoryMovies(private val movies: MutableMap<String, Movie> = mutableMapOf()) : Movies {

    override fun findById(id: String) = movies[id]

    override fun findAll() = movies.values.toList()
    override fun findByIdIn(ids: List<String>) = ids.mapNotNull { movies[it] }

    fun save(movie: Movie) {
        movies[movie.imdbId] = movie
    }
}