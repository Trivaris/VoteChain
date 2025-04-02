package com.trivaris.votechain.networking.messagehandlers

import com.trivaris.votechain.networking.MessageEnvelope
import com.trivaris.votechain.networking.Networking
import com.trivaris.votechain.voting.SerializableVote
import com.trivaris.votechain.voting.VotingManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import java.net.InetAddress

class VoteMessageHandler : MessageHandler {
    override fun outgoing(envelope: MessageEnvelope) {
        val recipient = InetAddress.getByName(envelope.recipient)

        println("[PEER] Sending vote")
        CoroutineScope(Dispatchers.IO).launch { Networking.send(envelope, recipient) }
    }
    override fun incoming(envelope: MessageEnvelope) {
        val message = envelope.message
        val vote = Json.decodeFromString<SerializableVote>(message.data)

        VotingManager.interpretVote(vote)
    }
}