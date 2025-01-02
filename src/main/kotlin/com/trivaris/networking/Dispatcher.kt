package com.trivaris.networking

import com.trivaris.blockchain.Block
import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.*
import io.ktor.client.statement.HttpResponse
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.json

object Dispatcher {
    val client = HttpClient {
        install(ContentNegotiation) {
            json()
        }
    }

    suspend fun post(body: Block, url: String, endpoint: String): HttpResponse =
        client.post("http://$url:8080/$endpoint") {
            contentType(ContentType.Application.Json)
            setBody(body) }

    suspend fun get(url: String, endpoint: String): HttpResponse =
        client.get("http://$url:8080/$endpoint") {
            contentType(ContentType.Application.Json) }
}