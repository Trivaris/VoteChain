package com.trivaris.votechain.networking.messagehandlers

import com.trivaris.votechain.Config
import com.trivaris.votechain.networking.MessageEnvelope
import com.trivaris.votechain.networking.NetworkManager
import com.trivaris.votechain.networking.Networking
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import java.net.InetAddress

class JoinResponseMessageHandler : MessageHandler {
    override fun outgoing(envelope: MessageEnvelope) {
        val recipient = InetAddress.getByName(envelope.recipient)

        println("[SERVER] Responding with participant list")
        CoroutineScope(Dispatchers.IO).launch { Networking.send(envelope, recipient) }
    }
    override fun incoming(envelope: MessageEnvelope) {
        if (envelope.originator != Config.data.serverIP) {
            println("[PEER] Got Bad Response from non-Server")
            NetworkManager.badRequester(envelope.originator)
            return
        }

        val message = envelope.message
        val listJson = message.data
        val list = Json.decodeFromString<MutableList<String>>(listJson)

        println("[PEER] Received participant list from Server: $list")
        NetworkManager.setParticipants(list)
    }
}