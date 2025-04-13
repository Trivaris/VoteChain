package com.trivaris.votechain.network.messagehandlers

import com.trivaris.votechain.common.Config
import com.trivaris.votechain.common.Logger
import com.trivaris.votechain.host.Server
import com.trivaris.votechain.model.block.ChainInteraction
import com.trivaris.votechain.network.MessageEnvelope
import com.trivaris.votechain.network.NetworkManager
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
        data.print()
        NetworkManager.setParticipants(data.participants)
        ChainInteraction.rebuild(data.blocks)

        NetworkManager.requestKeys()
    }
}