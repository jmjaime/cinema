package com.cinema.infrastructure.persistence.memory

import com.cinema.domain.rating.CustomerVote
import com.cinema.domain.rating.CustomerVotes

class InMemoryCustomerVotes(
    private val votes: MutableMap<Pair<String, String>, CustomerVote> = mutableMapOf()
) : CustomerVotes {

    override fun findByCustomerAndMovieId(customer: String, movieId: String) = votes[customer to movieId]

    override fun save(customerVote: CustomerVote) {
        votes[customerVote.customer to customerVote.movieId] = customerVote
    }

}
