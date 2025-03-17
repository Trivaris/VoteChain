package com.trivaris.votechain.networking

import com.trivaris.votechain.Server
import com.trivaris.votechain.blockchain.Block
import com.trivaris.votechain.blockchain.BlockManager
import com.trivaris.votechain.voting.SerializableVote
import com.trivaris.votechain.config
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

    fun outgoing(message: Message, receiver: InetAddress = Networking.broadcastingAddress) {
        when (message.type) {
            MessageType.JOIN_NETWORK -> {
                CoroutineScope(Dispatchers.IO).launch { networking.broadcast(message.toByteArray()) }
                println("Connected.")
                println("Starting Receiver...")
                CoroutineScope(Dispatchers.IO).launch { networking.startReceiver() }
            }

            MessageType.LEAVE_NETWORK -> {
                CoroutineScope(Dispatchers.IO).launch { networking.broadcast(message.toByteArray()) }
                println("Disconnected.")
                println("Stopping Receiver...")
                CoroutineScope(Dispatchers.IO).launch { networking.stopReceiver() }
            }
            MessageType.VOTE -> {
                println("Sending vote...")
                CoroutineScope(Dispatchers.IO).launch { networking.broadcast(message.toByteArray()) }
            }
            MessageType.BLOCK -> {
                println("Sending block...")
                CoroutineScope(Dispatchers.IO).launch { networking.broadcast(message.toByteArray()) }
            }
            MessageType.KEYS_REQUEST -> {
                println("Getting valid keys...")
                CoroutineScope(Dispatchers.IO).launch { networking.send(message.toByteArray(), receiver) }
            }
            MessageType.INVALID ->
                println("Invalid message: $message")
        }
    }

    fun incoming(message: Message) {
        when (message.type) {
            MessageType.JOIN_NETWORK -> {
                val address = InetAddress.getByName(message.originator)
                networkManager.participantJoined(address)
            }

            MessageType.LEAVE_NETWORK -> {
                val address = InetAddress.getByName(message.originator)
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
                when (message.data) {
                    "GET" -> {
                        val originator = InetAddress.getByName(message.originator)
                        server.keysRequest(originator)
                    }

                    else -> {
                        if (message.originator != config!!.data.serverIP)
                            networkManager.badRequester(InetAddress.getByName(message.originator))
                        else votingManager.decryptionMap = Json.decodeFromString<Map<String, String>>(message.data)
                    }
                }
            }

            MessageType.INVALID ->
                println("Invalid message: $message")

        }
    }

}