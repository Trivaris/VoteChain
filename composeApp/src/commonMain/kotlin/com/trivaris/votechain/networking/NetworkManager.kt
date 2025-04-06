package com.trivaris.votechain.networking

import com.trivaris.votechain.Logger
import com.trivaris.votechain.blockchain.database.BlockObject
import com.trivaris.votechain.networking.messagehandlers.MessageType
import com.trivaris.votechain.voting.SerializableVote

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
    fun broadcast(vote: SerializableVote) =
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

    fun clearParticipants() =
        participants.clear()

    fun participantJoined(address: String) =
        participants.add(address)

    fun participantLeft(address: String) =
        participants.remove(address)

    fun badRequester(address: String) =
        badRequesters.add(address)
}