package com.trivaris.votechain.networking

import com.trivaris.votechain.Config
import java.net.InetAddress

object NetworkManager {
    private val messageManager = MessageManager
    private val recipients = mutableListOf<InetAddress>()
    private val badRequesters = mutableListOf<InetAddress>()

    var ownAddress = ""

    fun join(ownAddress: String) {
        println("Own address $ownAddress")
        this.ownAddress = ownAddress

        val joinRequest = Message(MessageType.JOIN_NETWORK)
        val joinEnvelope = MessageEnvelope(joinRequest)
        messageManager.outgoing(joinEnvelope)

        val keyRequest = Message(MessageType.KEYS_REQUEST)
        val keysEnvelope = MessageEnvelope(keyRequest)
        messageManager.outgoing(keysEnvelope)
    }

    fun leave() {
        val message = Message(MessageType.LEAVE_NETWORK)
        messageManager.outgoing(message)
    }

    fun participantJoined(address: InetAddress) {
        println("New Participant: $address")
        recipients.add(address)
    }

    fun participantLeft(address: InetAddress) {
        println("Participant left: $address")
        recipients.remove(address)
    }

    fun badRequester(address: InetAddress) {
        println("Bad Requester: $address")
        badRequesters.add(address)
    }
}