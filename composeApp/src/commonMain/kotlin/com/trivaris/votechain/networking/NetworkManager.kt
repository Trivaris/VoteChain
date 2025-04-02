package com.trivaris.votechain.networking

import com.trivaris.votechain.networking.messagehandlers.MessageType

object NetworkManager {
    private val messageManager = MessageManager

    private var participants = mutableListOf<String>()
    private val badRequesters = mutableListOf<String>()

    var ownAddress = ""

    fun join(ownAddress: String) {
        println("[NETWORK] Own address $ownAddress")
        this.ownAddress = ownAddress

        val joinRequest = Message(MessageType.JOIN_REQUEST)
        messageManager.outgoing(joinRequest)

//        val keyRequest = Message(MessageType.KEYS_REQUEST)
//        messageManager.outgoing(keyRequest)
    }

    fun leave() {
        val message = Message(MessageType.LEAVE_NETWORK)
        messageManager.outgoing(message)
    }

    fun setParticipants(new: MutableList<String>) {
        participants = new
    }

    fun getParticipants() =
        participants

    fun participantJoined(address: String) =
        participants.add(address)

    fun participantLeft(address: String) =
        participants.remove(address)

    fun badRequester(address: String) =
        badRequesters.add(address)
}