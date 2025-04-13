package com.trivaris.votechain.network.messagehandlers

import com.trivaris.votechain.common.Config
import com.trivaris.votechain.common.Logger
import com.trivaris.votechain.network.MessageEnvelope
import com.trivaris.votechain.network.NetworkManager
import com.trivaris.votechain.vote.VotingManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.net.InetAddress

class KeysResponseMessageHandler: MessageHandler {
    override fun outgoing(envelope: MessageEnvelope) {
        val recipient = InetAddress.getByName(envelope.recipient)

        Logger.SERVER.log("Responding with Public Keys")
        CoroutineScope(Dispatchers.IO).launch { Networking.send(envelope, recipient) }
    }
    override fun incoming(envelope: MessageEnvelope) {
        if (envelope.originator != Config.data.serverIP) {
            Logger.PEER.log("Got Bad Response from non-Server")
            NetworkManager.badRequester(envelope.originator)
            return
        }

        val message = envelope.message
        val decryptionMap = Config.json.decodeFromString<Map<String, String>>(message.data)

        Logger.PEER.log("Received Public Keys from Server")
        VotingManager.setDecryptionMap(decryptionMap)
        VotingManager.updateVotes()
    }
}