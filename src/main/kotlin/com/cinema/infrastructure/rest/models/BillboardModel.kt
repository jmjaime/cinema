package com.cinema.infrastructure.rest.models

import com.cinema.domain.billboard.Billboard
import kotlinx.serialization.Serializable

@Serializable
data class BillboardModel(val availableMovies: List<MovieModel>)

fun Billboard.toModel() = BillboardModel(
    availableMovies = this.availableMovies.map { it.toModel() }
)