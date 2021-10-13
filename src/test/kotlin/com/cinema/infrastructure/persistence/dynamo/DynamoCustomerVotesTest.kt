package com.cinema.infrastructure.persistence.dynamo

import com.cinema.anyCustomerVote
import com.cinema.anyString
import com.cinema.domain.errors.MovieAlreadyRated
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider
import software.amazon.awssdk.services.dynamodb.DynamoDbClient
import software.amazon.awssdk.services.dynamodb.model.*
import java.net.URI
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNull

@Testcontainers
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class DynamoCustomerVotesTest {
    companion object {
        @Container
        @JvmStatic
        private val dynamoDbContainer = DynamoDbContainer()

        private const val tableName = "cinema"
    }

    private lateinit var dynamoDbClient: DynamoDbClient

    private lateinit var customerVotes: DynamoCustomerVotes

    @BeforeAll
    fun beforeAll() {
        dynamoDbClient = dynamoDbClient()
        createTable()
    }

    @BeforeEach
    fun setUp() {
        deleteAll()
        customerVotes = DynamoCustomerVotes(dynamoDbClient, tableName)
    }

    @Test
    fun `can save and retrieve a customer vote`() {
        val customerVote = anyCustomerVote()
        customerVotes.save(customerVote)
        assertEquals(customerVote, customerVotes.findByCustomerAndMovieId(customerVote.customer, customerVote.movieId))
    }

    @Test
    fun `customer can rate two movies`() {
        val votes = listOf(anyCustomerVote(), anyCustomerVote()).onEach {
            customerVotes.save(it)
        }
        votes.onEach {
            assertEquals(it, customerVotes.findByCustomerAndMovieId(it.customer, it.movieId))
        }
    }

    @Test
    fun `cannot save two times same customer vote`() {
        val customerVote = anyCustomerVote()
        customerVotes.save(customerVote)
        assertFailsWith<MovieAlreadyRated> {
            customerVotes.save(customerVote)
        }
    }

    @Test
    fun `when find customer vote and it does not exist returns null`() {
        assertNull(customerVotes.findByCustomerAndMovieId(anyString(), anyString()))
    }

    private fun dynamoDbClient() = DynamoDbClient.builder()
        .endpointOverride(URI.create(dynamoDbContainer.endpoint()))
        .credentialsProvider(StaticCredentialsProvider.create(AwsBasicCredentials.create("dummy", "dummy")))
        .build()

    private fun createTable() {
        dynamoDbClient.createTable {
            it
                .tableName(tableName)
                .billingMode(BillingMode.PAY_PER_REQUEST)
                .attributeDefinitions(
                    AttributeDefinition.builder()
                        .attributeName("PK")
                        .attributeType(ScalarAttributeType.S)
                        .build(),
                    AttributeDefinition.builder()
                        .attributeName("SK")
                        .attributeType(ScalarAttributeType.S)
                        .build(),
                ).keySchema(
                    KeySchemaElement.builder()
                        .attributeName("PK")
                        .keyType(KeyType.HASH)
                        .build(),
                    KeySchemaElement.builder()
                        .attributeName("SK")
                        .keyType(KeyType.RANGE)
                        .build(),
                ).build()
        }

        dynamoDbClient.waiter().waitUntilTableExists { it.tableName(tableName) }
    }

    private fun deleteAll() {
        dynamoDbClient.scanPaginator {
            it.tableName(tableName)
        }.forEach { scanResponse ->
            scanResponse.items().forEach { item ->
                dynamoDbClient.deleteItem {
                    it.tableName(tableName).key(mapOf("PK" to item["PK"], "SK" to item["SK"]))
                }
            }
        }
    }


}