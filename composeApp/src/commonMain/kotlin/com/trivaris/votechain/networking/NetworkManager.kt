package com.trivaris.votechain.networking

import com.trivaris.votechain.config
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
        messageManager.outgoing(joinRequest)
        val keyRequest = Message(MessageType.KEYS_REQUEST, data = "GET")
        messageManager.outgoing(keyRequest, InetAddress.getByName(config!!.data.serverIP))
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