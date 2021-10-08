package com.cinema.domain.rating

import com.cinema.anyString
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class CustomerVoteTest {

    @Test
    fun `can create valid customer vote`() {
        val customer = anyString()
        val comment = anyString()
        val movie = anyString()
        val vote = CustomerVote(customer = customer, movieId = movie, rating = Rating.FOUR, comment = comment)

        Assertions.assertEquals(customer, vote.customer)
        Assertions.assertEquals(movie, vote.movieId)
        Assertions.assertEquals(comment, vote.comment)
        Assertions.assertEquals(Rating.FOUR, vote.rating)
    }

    @Test
    fun `customer is mandatory`() {
        val error = assertThrows<IllegalStateException> {
            CustomerVote(
                customer = "",
                movieId = anyString(),
                rating = Rating.FOUR,
                comment = anyString()
            )
        }
        Assertions.assertEquals("customer is mandatory", error.message)
    }

    @Test
    fun `movie is mandatory`() {
        val error = assertThrows<IllegalStateException> {
            CustomerVote(
                customer = anyString(),
                movieId = "",
                rating = Rating.FOUR,
                comment = anyString()
            )
        }
        Assertions.assertEquals("movieId is mandatory", error.message)
    }
}