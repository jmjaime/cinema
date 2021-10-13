package com.cinema.infrastructure.persistence.dynamo

import com.cinema.domain.movie.Price
import com.cinema.domain.movie.showtimes.DailyShowtime
import com.cinema.domain.movie.showtimes.DailyShowtimes
import com.cinema.domain.movie.showtimes.Showtime
import com.cinema.infrastructure.rest.serializer.BigDecimalSerializer
import com.cinema.infrastructure.rest.serializer.LocalTimeSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import software.amazon.awssdk.services.dynamodb.DynamoDbClient
import software.amazon.awssdk.services.dynamodb.model.AttributeValue
import java.math.BigDecimal
import java.time.DayOfWeek
import java.time.LocalTime

class DynamoDailyShowtimes(
    private val dynamoDbClient: DynamoDbClient,
    private val tableName: String,
    private val mapper: Json
) : DailyShowtimes {

    companion object {
        private const val SHOWTIME = "SHOWTIME"
        private const val MOVIE = "MOVIE"
        private const val DAY = "DAY"
    }

    override fun findByMovieId(movieId: String): List<DailyShowtime> {
        return dynamoDbClient.query {
            it
                .tableName(tableName)
                .keyConditionExpression("${KeyColumns.PK} = :pk AND begins_with(${KeyColumns.SK}, :sk)")
                .expressionAttributeValues(
                    mapOf(
                        ":pk" to dailyShowtimePK(movieId),
                        ":sk" to s("DAY#")
                    )
                )
        }.let { response ->
            response.items().map { it.toDailyShowtime() }
        }
    }

    override fun findByMovieIdAndDay(movieId: String, day: DayOfWeek): DailyShowtime? {
        val response = dynamoDbClient.getItem {
            it.tableName(tableName)
                .key(
                    mapOf(
                        KeyColumns.PK to dailyShowtimePK(movieId),
                        KeyColumns.SK to dailyShowtimeSk(day)
                    )
                )
        }
        return response.takeIf { it.hasItem() }?.item()?.toDailyShowtime()
    }

    override fun save(dailyShowtime: DailyShowtime) {
        dynamoDbClient.putItem {
            it
                .tableName(tableName)
                .item(dailyShowtime.toItem())

        }
    }

    private fun dailyShowtimeSk(day: DayOfWeek) = s("DAY#${day.name}")

    private fun dailyShowtimePK(movieId: String) = s("MOVIE#$movieId")

    private fun Map<String, AttributeValue>.toDailyShowtime() =
        DailyShowtime(
            movieId = this[MOVIE]!!.s(),
            day = DayOfWeek.valueOf(this[DAY]!!.s()),
            showtimes = this[SHOWTIME]?.s()?.let { json ->
                mapper.decodeFromString<List<ShowtimeStorageModel>>(json).map { Showtime(it.startAt, Price(it.price)) }
            } ?: emptyList()
        )

    private fun DailyShowtime.toItem(): Map<String, AttributeValue> {
        return mapOf(
            KeyColumns.PK to dailyShowtimePK(movieId),
            KeyColumns.SK to dailyShowtimeSk(day),
            MOVIE to s(movieId),
            DAY to s(day.name),
            SHOWTIME to s(mapper.encodeToString(showtimes.map { ShowtimeStorageModel(it.startAt, it.price.amount) }))
        )
    }

    @Serializable
    private data class ShowtimeStorageModel(
        @Serializable(with = LocalTimeSerializer::class)
        val startAt: LocalTime,
        @Serializable(with = BigDecimalSerializer::class)
        val price: BigDecimal
    )
}