package com.trivaris.votechain.networking

import com.trivaris.votechain.Config
import com.trivaris.votechain.Server
import com.trivaris.votechain.blockchain.Block
import com.trivaris.votechain.blockchain.BlockManager
import com.trivaris.votechain.voting.SerializableVote
import com.trivaris.votechain.voting.VotingManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import java.net.InetAddress

object MessageManager {
    private val votingManager = VotingManager
    private val networkManager = NetworkManager
    private val blockManager = BlockManager
    private val networking = Networking
    private val server = Server

    fun outgoing(envelope: MessageEnvelope) {
        val message = envelope.message
        val receiver = envelope.recipient.replace("/", "")
        println(receiver)
        when (message.type) {
            MessageType.JOIN_NETWORK -> {
                CoroutineScope(Dispatchers.IO).launch { networking.send(envelope, InetAddress.getByName(Config.data.serverIP)) }
                println("Connected.")
                println("Starting Receiver...")
                CoroutineScope(Dispatchers.IO).launch { networking.startReceiver() }
            }

            MessageType.LEAVE_NETWORK -> {
                CoroutineScope(Dispatchers.IO).launch { networking.send(envelope) }
                println("Disconnected.")
                println("Stopping Receiver...")
                CoroutineScope(Dispatchers.IO).launch { networking.stopReceiver() }
            }
            MessageType.VOTE -> {
                println("Sending vote...")
                CoroutineScope(Dispatchers.IO).launch { networking.send(envelope) }
            }
            MessageType.BLOCK -> {
                println("Sending block...")
                CoroutineScope(Dispatchers.IO).launch { networking.send(envelope) }
            }
            MessageType.KEYS_REQUEST -> {
                println("Getting valid Keys")
                CoroutineScope(Dispatchers.IO).launch { networking.send(envelope) }
            }
            MessageType.KEYS_RESPONSE -> {
                println("Sending Keys")
                CoroutineScope(Dispatchers.IO).launch { networking.send(envelope, InetAddress.getByName(receiver)) }
            }
            MessageType.INVALID ->
                println("Invalid message: $message")
        }
    }

    fun incoming(envelope: MessageEnvelope) {
        val message = envelope.message
        when (message.type) {
            MessageType.JOIN_NETWORK -> {
                val address = InetAddress.getByName(envelope.originator)
                networkManager.participantJoined(address)
            }

            MessageType.LEAVE_NETWORK -> {
                val address = InetAddress.getByName(envelope.originator)
                networkManager.participantLeft(address)
            }

            MessageType.VOTE -> {
                val vote = Json.decodeFromString<SerializableVote>(message.data)
                votingManager.interpretVote(vote)
            }

            MessageType.BLOCK -> {
                val block = Json.decodeFromString<Block>(message.data)
                blockManager.newBlock(block)
            }

            MessageType.KEYS_REQUEST -> {
                val originator = InetAddress.getByName(envelope.originator)
                server.keysResponse(originator)
            }

            MessageType.KEYS_RESPONSE -> {
                if (envelope.originator != Config.data.serverIP)
                    networkManager.badRequester(InetAddress.getByName(envelope.originator))
                else {
                    val decryptionMap = Json.decodeFromString<Map<String, String>>(message.data)
                    votingManager.setDecryptionMap(decryptionMap)
                }
            }

            MessageType.INVALID ->
                println("Invalid message: $message")

        }
    }

}