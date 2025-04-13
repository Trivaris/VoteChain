package com.trivaris.votechain.network.messagehandlers

import com.trivaris.votechain.common.Config
import com.trivaris.votechain.common.Logger
import com.trivaris.votechain.host.Server
import com.trivaris.votechain.network.MessageEnvelope
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