package com.trivaris.votechain.networking.messagehandlers

import com.trivaris.votechain.Config
import com.trivaris.votechain.networking.MessageEnvelope
import com.trivaris.votechain.networking.NetworkManager
import com.trivaris.votechain.networking.Networking
import com.trivaris.votechain.voting.VotingManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import java.net.InetAddress

class KeysResponseMessageHandler : MessageHandler {
    override fun outgoing(envelope: MessageEnvelope) {
        val recipient = InetAddress.getByName(envelope.recipient)

        println("[SERVER] Responding with Public Keys")
        CoroutineScope(Dispatchers.IO).launch { Networking.send(envelope, recipient) }
    }
    override fun incoming(envelope: MessageEnvelope) {
        if (envelope.originator != Config.data.serverIP) {
            println("[PEER] Got Bad Response from non-Server")
            NetworkManager.badRequester(envelope.originator)
            return
        }

        val message = envelope.message
        val decryptionMap = Json.decodeFromString<Map<String, String>>(message.data)

        println("[PEER] Received Public Keys from Server")
        VotingManager.setDecryptionMap(decryptionMap)
    }
}