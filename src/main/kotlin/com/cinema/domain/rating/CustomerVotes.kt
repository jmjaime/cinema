package com.cinema.domain.rating

interface CustomerVotes {

    fun findByCustomerAndMovieId(customer: String, movieId: String): CustomerVote?

    fun save(customerVote: CustomerVote)

}