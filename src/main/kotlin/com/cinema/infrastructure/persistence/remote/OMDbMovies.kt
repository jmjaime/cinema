package com.cinema.infrastructure.persistence.remote

import com.cinema.domain.movie.IMDBRating
import com.cinema.domain.movie.Movie
import com.cinema.domain.movie.Movies
import com.github.benmanes.caffeine.cache.Cache
import com.github.benmanes.caffeine.cache.Caffeine
import java.util.concurrent.TimeUnit

class OMDbMovies(
    private val availableMovies: List<String>,
    cacheTimeOutInSeconds: Long,
    private val omdbClient: OMDbClient
) : Movies {

    private val moviesCache: Cache<String, Movie> =
        Caffeine.newBuilder().expireAfterWrite(cacheTimeOutInSeconds, TimeUnit.SECONDS).build()

    override fun findById(id: String): Movie? {
        return availableMovies.firstOrNull { it == id }?.let { getMovie(it) }
    }

    override fun findAll(): List<Movie> {
        return availableMovies.mapNotNull { getMovie(it) }
    }

    override fun findByIdIn(ids: List<String>): List<Movie> {
        return availableMovies.filter { it in ids }.mapNotNull { getMovie(it) }
    }

    private fun getMovie(id: String): Movie? {
        return moviesCache.get(id) {
            omdbClient.getMovie(it)?.let { movieOMDb ->
                Movie(
                    imdbId = movieOMDb.imdbID,
                    name = movieOMDb.Title,
                    releaseDate = movieOMDb.Released,
                    runtime = movieOMDb.Runtime,
                    imdbRating = IMDBRating(
                        rating = movieOMDb.imdbRating,
                        votes = movieOMDb.imdbVotes
                    )
                )
            }
        }
    }

}