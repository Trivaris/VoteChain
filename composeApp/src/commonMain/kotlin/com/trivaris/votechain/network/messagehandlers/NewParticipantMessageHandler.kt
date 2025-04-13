package com.trivaris.votechain.network.messagehandlers

import com.trivaris.votechain.common.Config
import com.trivaris.votechain.common.Logger
import com.trivaris.votechain.network.MessageEnvelope
import com.trivaris.votechain.network.NetworkManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.net.InetAddress

class NewParticipantMessageHandler : MessageHandler {
    override fun outgoing(envelope: MessageEnvelope) {
        val recipient = InetAddress.getByName(envelope.recipient)

        Logger.SERVER.log("Broadcasting New Participant to: ${envelope.recipient}")
        CoroutineScope(Dispatchers.IO).launch { Networking.send(envelope, recipient) }
    }

    override fun incoming(envelope: MessageEnvelope) {
        if (envelope.originator != Config.data.serverIP) return

        val message = envelope.message
        val newParticipant = message.data

        Logger.PEER.log("New Participant: $newParticipant")
        NetworkManager.participantJoined(newParticipant)
    }
}