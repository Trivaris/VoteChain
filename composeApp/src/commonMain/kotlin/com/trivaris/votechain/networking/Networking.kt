package com.trivaris.votechain.networking

import com.trivaris.votechain.Config
import com.trivaris.votechain.Logger
import java.net.InetAddress
import kotlinx.coroutines.*
import kotlinx.serialization.json.Json
import java.net.ConnectException
import java.net.ServerSocket
import java.net.Socket
import java.net.SocketException

object Networking {
    private var serverSocket: ServerSocket = ServerSocket(Config.data.receivingPort).apply { close() }
    private val scope = CoroutineScope(Dispatchers.IO)

    fun startServer() {
        synchronized(this) {
            if (!serverSocket.isClosed) {
                Logger.NETWORK.log("Server is already running.")
                return
            }
            serverSocket = ServerSocket(Config.data.receivingPort)
        }

        Logger.NETWORK.log("Starting server on port ${Config.data.receivingPort}...")
        scope.launch {
            try {
                while (true) {
                    val client = serverSocket.accept()
                    val input = client.inputStream
                    val data = input.readBytes().decodeToString()
                    val envelope = Json.decodeFromString<MessageEnvelope>(data)

                    MessageManager.incoming(envelope)
                }
            } catch (_: Exception) {
                Logger.NETWORK.log("Server closed")
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
                output.write(Json.encodeToString(envelope).toByteArray())
                output.flush()
                socket.close()
            } catch (_: Exception) {
                Logger.NETWORK.log("Did not send ${envelope.message.type}, server is not running")
            }
        }
    }

}