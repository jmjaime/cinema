package com.cinema.domain.errors

import java.time.LocalDateTime

class ShowTimeAlreadyExists(movieId: String, startAt: LocalDateTime) :
    RuntimeException("Already exists showtime for movie $movieId at $startAt")
