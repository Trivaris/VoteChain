package com.trivaris.votechain.networking.messagehandlers

import com.trivaris.votechain.networking.MessageEnvelope
import com.trivaris.votechain.networking.NetworkManager
import com.trivaris.votechain.networking.Networking
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LeaveNetworkMessageHandler : MessageHandler {
    override fun outgoing(envelope: MessageEnvelope) {
        println("[PEER] Sent Request to leave Network \n" +
                "[PEER] Stopped Listening" )
        CoroutineScope(Dispatchers.IO).launch { Networking.send(envelope) }
        CoroutineScope(Dispatchers.IO).launch { Networking.stopReceiver() }
    }
    override fun incoming(envelope: MessageEnvelope) {
        val address = envelope.originator

        println("[SERVER] Participant left: $address")
        NetworkManager.participantLeft(address)
    }
}