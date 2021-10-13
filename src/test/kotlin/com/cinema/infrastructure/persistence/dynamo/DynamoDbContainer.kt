package com.cinema.infrastructure.persistence.dynamo

import org.testcontainers.containers.GenericContainer

class DynamoDbContainer : GenericContainer<DynamoDbContainer>("amazon/dynamodb-local:1.16.0") {

    companion object {
        private const val dynamoDbLocalDefaultPort = 8000
    }

    init {
        withExposedPorts(dynamoDbLocalDefaultPort)
    }

    fun endpoint(): String = "http://${containerIpAddress}:${getMappedPort(dynamoDbLocalDefaultPort)}"

}