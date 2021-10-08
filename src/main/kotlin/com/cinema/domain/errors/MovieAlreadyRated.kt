package com.cinema.domain.errors

class MovieAlreadyRated(movieId: String) : RuntimeException("Movie $movieId already rated")
