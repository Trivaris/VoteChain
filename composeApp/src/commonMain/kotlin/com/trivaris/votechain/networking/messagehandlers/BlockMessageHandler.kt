package com.trivaris.votechain.networking.messagehandlers

import com.trivaris.votechain.Config
import com.trivaris.votechain.Logger
import com.trivaris.votechain.store.block.BlockObject
import com.trivaris.votechain.store.block.BlockRepository
import com.trivaris.votechain.networking.MessageEnvelope
import com.trivaris.votechain.networking.Networking
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
        BlockRepository.addBlock(block)
    }
}