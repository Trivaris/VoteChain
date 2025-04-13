package com.trivaris.votechain.network

import com.trivaris.votechain.common.Logger
import com.trivaris.votechain.model.block.BlockObject
import com.trivaris.votechain.network.messagehandlers.MessageType
import com.trivaris.votechain.vote.Vote

object NetworkManager {
    private var participants = mutableSetOf<String>()
    private val badRequesters = mutableSetOf<String>()

    var ownAddress = ""

    fun join(ownAddress: String = this.ownAddress) {
        Logger.NETWORK.log("Own address $ownAddress")
        this.ownAddress = ownAddress

        val joinRequest = Message(MessageType.JOIN_REQUEST)
        MessageManager.outgoing(joinRequest)
    }

    fun leave() {
        val message = Message(MessageType.LEAVE_NETWORK)
        MessageManager.outgoing(message)
    }

    fun requestKeys() {
        val message = Message(MessageType.KEYS_REQUEST)
        MessageManager.outgoing(message)
    }

    fun broadcast(block: BlockObject) =
        broadcast(Message(block))
    fun broadcast(vote: Vote) =
        broadcast(Message(vote))

    private fun broadcast(message: Message) {
        Logger.NETWORK.log("Broadcasting to: $participants")
        participants.forEach {
            val envelope = MessageEnvelope(message, it)
            MessageManager.outgoing(envelope)
        }
    }

    fun setParticipants(new: MutableSet<String>) {
        Logger.PEER.log("Setting Participants to $new")
        participants = new
    }

    fun clearParticipants() {
        Logger.DEBUG.log("Clearing Participants")
        participants.clear()
    }

    fun participantJoined(address: String) =
        participants.add(address)

    fun participantLeft(address: String) =
        participants.remove(address)

    fun badRequester(address: String) =
        badRequesters.add(address)
}