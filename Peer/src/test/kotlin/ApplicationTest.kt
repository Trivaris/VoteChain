package com.trivaris.votechain.peer.test

import com.trivaris.votechain.peer.modulePeer
import io.ktor.client.HttpClient
import io.ktor.client.plugins.websocket.WebSockets
import io.ktor.server.testing.*
import kotlin.test.*

class ApplicationTest {
    @Test
    fun testRoot() = testApplication {
        application {
            modulePeer()
        }

        val client = HttpClient {
            install(WebSockets)
        }


    }
}