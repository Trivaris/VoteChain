package com.trivaris.votechain.networking

import com.trivaris.votechain.Config
import kotlinx.coroutines.Job
import java.net.DatagramPacket
import java.net.DatagramSocket
import java.net.InetAddress
import kotlinx.coroutines.*
import kotlinx.serialization.json.Json
import java.net.NetworkInterface

const val PORT = 9235
const val BUFFER_SIZE = 8192

object Networking {
    private val socket = DatagramSocket(PORT)
    private var receiverJob: Job = Job().apply { cancel() }
    private val json = Json {
        prettyPrint = true
        encodeDefaults = true
    }

    fun send(message: Message, address: InetAddress = InetAddress.getByName(Config.data.serverIP)) =
        send(MessageEnvelope(message), address)
    fun send(envelope: MessageEnvelope, address: InetAddress = InetAddress.getByName(Config.data.serverIP)) {
        val json = json.encodeToString(envelope)
        val data = "${json.length}::$json"
        println("Sending $json")
        val encoded = data.toByteArray()
        val packet = DatagramPacket(encoded, encoded.size, address, PORT)
        socket.send(packet)
    }

    fun startReceiver() {
        receiverJob = CoroutineScope(Dispatchers.IO).launch {
            val buffer = ByteArray(BUFFER_SIZE)
            val packet = DatagramPacket(buffer, buffer.size)

            while (isActive) {
                socket.receive(packet)
                val raw = packet.data.decodeToString()
                val length = raw.substringBefore("::").toInt()
                val json = raw.substringAfter("::").substring(0..<length)
                println("Received: $json")
                val envelope = Json.decodeFromString<MessageEnvelope>(json)
                envelope.originator = packet.address.hostAddress
                MessageManager.incoming(envelope)
            }
        }
    }

    fun stopReceiver() =
        receiverJob.cancel()

    fun isConnected(): Boolean =
        receiverJob.isActive
}