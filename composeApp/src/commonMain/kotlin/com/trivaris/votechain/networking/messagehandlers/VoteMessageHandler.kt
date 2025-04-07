package com.trivaris.votechain.networking.messagehandlers

import com.trivaris.votechain.Config
import com.trivaris.votechain.Logger
import com.trivaris.votechain.networking.MessageEnvelope
import com.trivaris.votechain.networking.NetworkManager
import com.trivaris.votechain.networking.Networking
import com.trivaris.votechain.voting.SerializableVote
import com.trivaris.votechain.voting.VotingManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.net.InetAddress

class VoteMessageHandler : MessageHandler {
    override fun outgoing(envelope: MessageEnvelope) {
        val recipient = InetAddress.getByName(envelope.recipient)

        Logger.PEER.log("Sending vote")
        CoroutineScope(Dispatchers.IO).launch { Networking.send(envelope, recipient) }
    }
    override fun incoming(envelope: MessageEnvelope) {
        val message = envelope.message
        val vote = Config.json.decodeFromString<SerializableVote>(message.data)

        VotingManager.interpretVote(vote)
        if (VotingManager.isDecryptionMapEmpty) {
            NetworkManager.requestKeys()
        } else VotingManager.updateVotes()
    }
}