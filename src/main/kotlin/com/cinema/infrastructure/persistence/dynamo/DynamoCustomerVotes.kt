package com.cinema.infrastructure.persistence.dynamo

import com.cinema.domain.errors.MovieAlreadyRated
import com.cinema.domain.rating.CustomerVote
import com.cinema.domain.rating.CustomerVotes
import com.cinema.domain.rating.Rating
import com.cinema.infrastructure.persistence.dynamo.KeyColumns.PK
import com.cinema.infrastructure.persistence.dynamo.KeyColumns.SK
import software.amazon.awssdk.services.dynamodb.DynamoDbClient
import software.amazon.awssdk.services.dynamodb.model.AttributeValue
import software.amazon.awssdk.services.dynamodb.model.ConditionalCheckFailedException

class DynamoCustomerVotes(
    private val dynamoDbClient: DynamoDbClient,
    private val tableName: String
) : CustomerVotes {

    companion object {
        private const val CUSTOMER = "CUSTOMER"
        private const val MOVIE = "MOVIE"
        private const val RATING = "RATING"
        private const val COMMENTS = "COMMENTS"
        private const val UNIQUE_CUSTOMER_VOTE_PK = "attribute_not_exists(PK) and attribute_not_exists(SK)"
    }

    override fun findByCustomerAndMovieId(customer: String, movieId: String): CustomerVote? {
        val response = dynamoDbClient.getItem {
            it.tableName(tableName)
                .key(
                    mapOf(
                        PK to votePk(movieId),
                        SK to voteSk(customer)
                    )
                )
        }

        return response.takeIf { it.hasItem() }?.item()?.toCustomerVote()
    }

    override fun save(customerVote: CustomerVote) {
        try {
            dynamoDbClient.putItem {
                it
                    .tableName(tableName)
                    .item(customerVote.toItem())
                    .conditionExpression(UNIQUE_CUSTOMER_VOTE_PK)
            }
        } catch (e: ConditionalCheckFailedException) {
            throw MovieAlreadyRated(customerVote.movieId)
        }
    }

    private fun voteSk(customer: String) = s("VOTE#$customer")

    private fun votePk(movieId: String) = s("MOVIE#$movieId")

    private fun Map<String, AttributeValue>.toCustomerVote() =
        CustomerVote(
            customer = this[CUSTOMER]!!.s(),
            movieId = this[MOVIE]!!.s(),
            rating = Rating.valueOf(this[RATING]!!.s()),
            comment = this[COMMENTS]?.s()
        )

    private fun CustomerVote.toItem(): Map<String, AttributeValue> {
        return mapOf(
            PK to votePk(movieId),
            SK to voteSk(customer),
            MOVIE to s(movieId),
            CUSTOMER to s(customer),
            RATING to s(rating.name)
        ).plus(
            comment?.let {
                mapOf(COMMENTS to s(comment))
            } ?: emptyMap()
        )
    }
}