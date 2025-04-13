package com.trivaris.votechain.network.messagehandlers

import com.trivaris.votechain.common.Config
import com.trivaris.votechain.common.Logger
import com.trivaris.votechain.network.MessageEnvelope
import com.trivaris.votechain.network.NetworkManager
import com.trivaris.votechain.vote.Vote
import com.trivaris.votechain.vote.VotingManager
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
        val vote = Config.json.decodeFromString<Vote>(message.data)

        VotingManager.interpretVote(vote)
        if (VotingManager.isDecryptionMapEmpty) {
            NetworkManager.requestKeys()
        } else VotingManager.updateVotes()
    }
}