package com.trivaris.networking

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

    suspend fun post(body: Any, peer: String, endpoint: String): HttpResponse =
        client.post("http://$peer:8080/$endpoint") {
            contentType(ContentType.Application.Json)
            setBody(body)
        }

    suspend fun get(peer: String, endpoint: String): HttpResponse =
        client.get("http://$peer:8080/$endpoint") {
            contentType(ContentType.Application.Json)
        }


    suspend fun post(body: Any, peers: List<String>, endpoint: String): Array<HttpResponse> {
        val responses = arrayOf<HttpResponse>()
        for (peer in peers) {
            val response = post(body, peer, endpoint)
            responses.plus(response)
        }
        return responses
    }
}