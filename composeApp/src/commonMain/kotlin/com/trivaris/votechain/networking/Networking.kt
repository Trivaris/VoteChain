package com.trivaris.votechain.networking

import com.trivaris.votechain.Config
import com.trivaris.votechain.Logger
import java.net.InetAddress
import kotlinx.coroutines.*
import java.net.ServerSocket
import java.net.Socket

object Networking {
    private var serverSocket = ServerSocket(Config.data.receivingPort).apply { close() }
    private val scope = CoroutineScope(Dispatchers.IO)

    fun startServer() {
        synchronized(this) {
            if (!serverSocket.isClosed) {
                Logger.NETWORK.log("Server is already running.")
                return
            }
            serverSocket = ServerSocket(Config.data.receivingPort)
            Logger.NETWORK.log("Server is running: ${!serverSocket.isClosed}")
        }

        Logger.NETWORK.log("Starting server on port ${Config.data.receivingPort}...")
        scope.launch {
            try {
                while (true) {
                    val client = serverSocket.accept()
                    val input = client.inputStream
                    val data = input.readBytes().decodeToString()
                    val envelope = Config.json.decodeFromString<MessageEnvelope>(data)
                    envelope.originator = client.inetAddress.hostAddress

                    MessageManager.incoming(envelope)
                }
            } catch (e: Exception) {
                Logger.NETWORK.log("There was an Error in Receiving: ${e.message}")
            }
        }
    }

    fun stopServer() {
        synchronized(this) {
            if (serverSocket.isClosed) {
                Logger.NETWORK.log("Server is not running.")
                return
            }
            serverSocket.close()
        }
    }

    fun send(envelope: MessageEnvelope, address: InetAddress = InetAddress.getByName(Config.data.serverIP)) {
        scope.launch {
            try {
                val socket = Socket(address, Config.data.serverPort)
                val output = socket.getOutputStream()
                Logger.NETWORK.log("Sending ${envelope.message.type} to $address")
                output.write(Config.json.encodeToString(envelope).toByteArray())
                output.flush()
                socket.close()
            } catch (e: Exception) {
                Logger.NETWORK.log("There was an Error in Sending: ${e.message}")
            }
        }
    }

}