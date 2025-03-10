package com.trivaris.votechain

import io.ktor.server.application.*
import io.ktor.server.routing.*
import io.ktor.server.websocket.*
import io.ktor.websocket.Frame.Text
import io.ktor.websocket.readText
import java.util.UUID
import com.trivaris.votechain.Connection.*
import io.ktor.server.plugins.origin
import kotlinx.coroutines.channels.consumeEach

fun Application.configureSockets() {
    install(WebSockets)

    val clients = mutableMapOf<String, DefaultWebSocketServerSession>()

    routing {
        webSocket("/client") {
            val clientId = UUID.randomUUID().toString()

            clients.values.forEach { it.send(PEER_JOINED.withMessage(clientId)) }
            send(PEER_LIST.withMessage(clients.keys.joinToString(",")))

            clients[clientId] = this
            println("Client connected: $clientId")

            try {
                incoming.consumeEach { frame ->
                    frame as? Text ?: return@consumeEach
                    val message = frame.readText()

                    when {
                        message.isType(CONNECT_REQUEST) -> {
                            val targetId = message.getData(CONNECT_REQUEST)
                            val target = clients[targetId]
                            if (target == null) {
                                send(INVALID_REQUEST.withMessage("Peer not found"))
                                return@consumeEach
                            }

                            val requesterIp = call.request.origin.remoteHost
                            val requesterPort = 0

                            target.send(CONNECT_REQUEST.withMessage("$requesterIp:$requesterPort"))
                            send(CONNECT_INFO.withMessage("${target.call.ip}:${target.call.port}"))
                        }

                    }
                }
            } finally {
                clients.remove(clientId)
                clients.values.forEach { it.send(PEER_LEFT.withMessage(clientId)) }
            }
        }
    }
}
