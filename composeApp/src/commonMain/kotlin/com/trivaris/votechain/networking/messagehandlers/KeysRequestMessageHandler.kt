package com.trivaris.votechain.networking.messagehandlers

import com.trivaris.votechain.Config
import com.trivaris.votechain.Logger
import com.trivaris.votechain.Server
import com.trivaris.votechain.networking.MessageEnvelope
import com.trivaris.votechain.networking.Networking
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class KeysRequestMessageHandler : MessageHandler {
    override fun outgoing(envelope: MessageEnvelope) {
        Logger.PEER.log("Requesting Public Keys")
        CoroutineScope(Dispatchers.IO).launch { Networking.send(envelope) }
    }
    override fun incoming(envelope: MessageEnvelope) {
        if (!Config.data.isServer) return

        val requester = envelope.originator

        Logger.PEER.log("Incoming Request for Public Keys")
        Server.keysResponse(requester)
    }
}