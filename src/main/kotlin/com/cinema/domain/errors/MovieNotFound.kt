package com.cinema.domain.errors

class MovieNotFound(movieId: String) : RuntimeException("Movie $movieId not found")
