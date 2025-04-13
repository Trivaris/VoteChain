package com.trivaris.votechain.network.messagehandlers

import com.trivaris.votechain.common.Logger
import com.trivaris.votechain.network.MessageEnvelope
import com.trivaris.votechain.network.NetworkManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LeaveNetworkMessageHandler : MessageHandler {
    override fun outgoing(envelope: MessageEnvelope) {
        Logger.PEER.log("Sent Request to leave Network", "Stopped Listening" )
        CoroutineScope(Dispatchers.IO).launch {
            Networking.send(envelope)
            Networking.stopServer()
        }
    }
    override fun incoming(envelope: MessageEnvelope) {
        val address = envelope.originator

        Logger.SERVER.log("Participant left: $address")
        NetworkManager.participantLeft(address)
    }
}