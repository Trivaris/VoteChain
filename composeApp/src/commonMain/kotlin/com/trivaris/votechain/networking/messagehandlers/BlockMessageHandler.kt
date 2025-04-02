package com.trivaris.votechain.networking.messagehandlers

import com.trivaris.votechain.blockchain.Block
import com.trivaris.votechain.blockchain.BlockManager
import com.trivaris.votechain.networking.MessageEnvelope
import com.trivaris.votechain.networking.Networking
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import java.net.InetAddress

class BlockMessageHandler : MessageHandler {
    override fun outgoing(envelope: MessageEnvelope) {
        val recipient = InetAddress.getByName(envelope.recipient)

        println("[PEER] Sending block...")
        CoroutineScope(Dispatchers.IO).launch { Networking.send(envelope, recipient) }
    }
    override fun incoming(envelope: MessageEnvelope) {
        val message = envelope.message
        val block = Json.decodeFromString<Block>(message.data)

        println("[PEER] Received block: ${block.hash}")
        BlockManager.newBlock(block)
    }
}