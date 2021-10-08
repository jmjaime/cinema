package com.cinema.domain.rating

class InMemoryCustomerVotes(private val votes: MutableMap<Pair<String, String>, CustomerVote> = mutableMapOf()) :
    CustomerVotes {

    override fun findByCustomerAndMovieId(customer: String, movieId: String) = votes[customer to movieId]

    override fun save(customerVote: CustomerVote) {
        votes[customerVote.customer to customerVote.movieId] = customerVote
    }

}
