package com.trivaris.votechain.network.messagehandlers

import com.trivaris.votechain.common.Config
import com.trivaris.votechain.common.Logger
import com.trivaris.votechain.host.Server
import com.trivaris.votechain.network.MessageEnvelope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class JoinRequestMessageHandler : MessageHandler {
    override fun outgoing(envelope: MessageEnvelope) {
        Logger.PEER.log("Started Listening", "Sent Request to join Network")
        CoroutineScope(Dispatchers.IO).launch {
            Networking.startServer()
            Networking.send(envelope)
        }
    }
    override fun incoming(envelope: MessageEnvelope) {
        if (!Config.data.isServer) return

        val newParticipant = envelope.originator

        Logger.SERVER.log("New Participant: $newParticipant")
        Server.joinResponse(newParticipant)
    }
}