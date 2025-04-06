package com.trivaris.votechain.networking.messagehandlers

import com.trivaris.votechain.Logger
import com.trivaris.votechain.networking.MessageEnvelope
import com.trivaris.votechain.networking.NetworkManager
import com.trivaris.votechain.networking.Networking
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LeaveNetworkMessageHandler : MessageHandler {
    override fun outgoing(envelope: MessageEnvelope) {
        Logger.PEER.log("Sent Request to leave Network", "Stopped Listening" )
        CoroutineScope(Dispatchers.IO).launch { Networking.send(envelope) }
        CoroutineScope(Dispatchers.IO).launch { Networking.stopServer() }
    }
    override fun incoming(envelope: MessageEnvelope) {
        val address = envelope.originator

        Logger.SERVER.log("Participant left: $address")
        NetworkManager.participantLeft(address)
    }
}