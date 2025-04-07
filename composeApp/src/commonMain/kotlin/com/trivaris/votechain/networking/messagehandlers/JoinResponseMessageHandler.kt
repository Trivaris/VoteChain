package com.trivaris.votechain.networking.messagehandlers

import com.trivaris.votechain.Config
import com.trivaris.votechain.Logger
import com.trivaris.votechain.Server
import com.trivaris.votechain.store.block.BlockRepository
import com.trivaris.votechain.networking.MessageEnvelope
import com.trivaris.votechain.networking.NetworkManager
import com.trivaris.votechain.networking.Networking
import com.trivaris.votechain.voting.VotingManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.net.InetAddress

class JoinResponseMessageHandler : MessageHandler {
    override fun outgoing(envelope: MessageEnvelope) {
        val recipient = InetAddress.getByName(envelope.recipient)

        Logger.SERVER.log("Responding with participant list")
        CoroutineScope(Dispatchers.IO).launch { Networking.send(envelope, recipient) }
    }
    override fun incoming(envelope: MessageEnvelope) {
        if (envelope.originator != Config.data.serverIP) {
            Logger.PEER.log("Got Bad Response from non-Server")
            NetworkManager.badRequester(envelope.originator)
            return
        }

        val message = envelope.message
        val dataJson = message.data
        val data = Config.json.decodeFromString<Server.JoinData>(dataJson)

        if (Config.isAndroid) Logger.PEER.log("Received Info")
        Logger.PEER.log("Received info:", data.participants, "Blocks: ${data.blocks.size}", "Current Votes: ${data.currentVotes.size}", showOnAndroid = false)
        NetworkManager.setParticipants(data.participants)
        VotingManager.setCurrentVotes(data.currentVotes)
        BlockRepository.setBlocks(data.blocks)

        NetworkManager.requestKeys()
    }
}