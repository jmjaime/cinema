package com.cinema.domain.rating

data class CustomerVote(val customer: String, val movieId: String, val rating: Rating, val comment: String) {
    init {
        check(customer.isNotBlank()) { "customer is mandatory" }
        check(movieId.isNotBlank()) { "movieId is mandatory" }
    }
}