package com.cinema.infrastructure.persistence.dynamo

import com.cinema.anyDailyShowtime
import com.cinema.anyString
import kotlinx.serialization.json.Json
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
import java.time.DayOfWeek
import kotlin.test.assertEquals

@Testcontainers
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class DynamoDailyShowtimeTest {
    companion object {
        @Container
        @JvmStatic
        private val dynamoDbContainer = DynamoDbContainer()

        private const val tableName = "cinema"
    }

    private lateinit var dynamoDbClient: DynamoDbClient

    private lateinit var dynamoDailyShowtimes: DynamoDailyShowtimes

    @BeforeAll
    fun beforeAll() {
        dynamoDbClient = dynamoDbClient()
        createTable()
    }

    @BeforeEach
    fun setUp() {
        deleteAll()
        dynamoDailyShowtimes = DynamoDailyShowtimes(dynamoDbClient, tableName, Json)
    }

    @Test
    fun `can save and retrieve a customer vote`() {
        val dailyShowtime = anyDailyShowtime(anyString())
        dynamoDailyShowtimes.save(dailyShowtime)
        assertEquals(dailyShowtime, dynamoDailyShowtimes.findByMovieIdAndDay(dailyShowtime.movieId, dailyShowtime.day))
    }

    @Test
    fun `can find all daily showtimes for a movie`() {
        val movieId = anyString()
        val movieShowtimes =
            listOf(
                anyDailyShowtime(movieId, DayOfWeek.FRIDAY),
                anyDailyShowtime(movieId, DayOfWeek.MONDAY)
            ).onEach { dynamoDailyShowtimes.save(it) }

        assertEquals(movieShowtimes, dynamoDailyShowtimes.findByMovieId(movieId))
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