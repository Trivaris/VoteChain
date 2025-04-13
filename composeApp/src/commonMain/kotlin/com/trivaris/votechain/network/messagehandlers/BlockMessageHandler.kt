package com.trivaris.votechain.network.messagehandlers

import com.trivaris.votechain.common.Config
import com.trivaris.votechain.common.Logger
import com.trivaris.votechain.model.block.BlockObject
import com.trivaris.votechain.model.block.ChainInteraction
import com.trivaris.votechain.network.MessageEnvelope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.net.InetAddress

class BlockMessageHandler : MessageHandler {
    override fun outgoing(envelope: MessageEnvelope) {
        val recipient = InetAddress.getByName(envelope.recipient)

        Logger.PEER.log("Sending block...")
        CoroutineScope(Dispatchers.IO).launch { Networking.send(envelope, recipient) }
    }
    override fun incoming(envelope: MessageEnvelope) {
        val message = envelope.message
        val block = Config.json.decodeFromString<BlockObject>(message.data)

        Logger.PEER.log("Received block: ${block.hash}")
        ChainInteraction.addBlock(block)
    }
}