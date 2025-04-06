package com.trivaris.votechain.networking.messagehandlers

import com.trivaris.votechain.Config
import com.trivaris.votechain.Logger
import com.trivaris.votechain.Server
import com.trivaris.votechain.networking.MessageEnvelope
import com.trivaris.votechain.networking.Networking
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class JoinRequestMessageHandler : MessageHandler {
    override fun outgoing(envelope: MessageEnvelope) {
        Logger.PEER.log("Started Listening", "Sent Request to join Network")
        CoroutineScope(Dispatchers.IO).launch { Networking.startServer() }
        CoroutineScope(Dispatchers.IO).launch { Networking.send(envelope) }
    }
    override fun incoming(envelope: MessageEnvelope) {
        if (!Config.data.isServer) return

        val newParticipant = envelope.originator

        Logger.SERVER.log("New Participant: $newParticipant")
        Server.joinResponse(newParticipant)
    }
}