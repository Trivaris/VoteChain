package com.trivaris.votechain.networking.messagehandlers

import com.trivaris.votechain.Config
import com.trivaris.votechain.Logger
import com.trivaris.votechain.networking.MessageEnvelope
import com.trivaris.votechain.networking.NetworkManager
import com.trivaris.votechain.networking.Networking
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