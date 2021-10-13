package com.cinema.infrastructure.persistence.dynamo

import software.amazon.awssdk.services.dynamodb.model.AttributeValue

object KeyColumns {
    const val PK = "PK"
    const val SK = "SK"
}

fun s(s: String): AttributeValue = AttributeValue.builder().s(s).build()
