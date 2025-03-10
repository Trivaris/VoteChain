package com.trivaris.votechain.server.test

import com.trivaris.votechain.isType
import com.trivaris.votechain.moduleServer
import io.ktor.client.HttpClient
import com.trivaris.votechain.Connection.*
import com.trivaris.votechain.getData
import io.ktor.server.testing.*
import io.ktor.websocket.Frame
import io.ktor.websocket.readText
import kotlin.test.*
import io.ktor.client.plugins.websocket.*
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class ApplicationTest {
    @Test
    fun testRoot() = testApplication {
        application {
            moduleServer()
        }
        val clients = mutableListOf<String>()

        val client = HttpClient {
            install(WebSockets)
        }

        runBlocking {
            val firstJob = launch {
                client.webSocket("ws://127.0.0.1:8080/client") {
                    incoming.consumeEach { frame ->
                        frame as? Frame.Text ?: return@consumeEach
                        val message = frame.readText()
                        when {
                            message.isType(PEER_JOINED) -> {
                                val newClientId = message.getData(PEER_JOINED)
                                println("New client joined: $newClientId")
                                clients.add(newClientId)
                            }

                            message.isType(CONNECT_REQUEST) -> {
                                val peerIp = message.getData(CONNECT_REQUEST)
                                println("Connecting to peer: $peerIp")
                            }

                            message.isType(PEER_LEFT) -> {
                                val clientId = message.getData(PEER_LEFT)
                                clients.remove(clientId)
                            }
                        }

                    }
                }
            }
            delay(1000)
            val secondJob = launch {
                client.webSocket("ws://127.0.0.1:8080/client") {

                }
            }
        }
    }
}