package com.trivaris.votechain.networking

import kotlinx.coroutines.Job
import java.net.DatagramPacket
import java.net.DatagramSocket
import java.net.InetAddress
import kotlinx.coroutines.*
import java.net.NetworkInterface

const val PORT = 9235
const val BUFFER_SIZE = 8192

object Networking {
    private val socket = DatagramSocket(PORT)
    private var receiverJob: Job = Job().apply { cancel() }
    var broadcastingAddress: InetAddress = InetAddress.getByName("255.255.255.255")

    fun broadcast(message: ByteArray) =
        send(message, broadcastingAddress)
    fun send(message: ByteArray, address: InetAddress) {
        val packet = DatagramPacket(message, message.size, address, PORT)
        socket.send(packet)
    }

    fun startReceiver() {
        receiverJob = CoroutineScope(Dispatchers.IO).launch {
            val buffer = ByteArray(BUFFER_SIZE)
            val packet = DatagramPacket(buffer, buffer.size)

            while (isActive) {
                socket.receive(packet)
                val message = Message(packet.data)
                println("Received ${message.type}!")
                MessageManager.incoming(message)
            }
        }
    }

    fun updateBroadcastingAddress() {
        val networkInterfaces = NetworkInterface.getNetworkInterfaces()
        while (networkInterfaces.hasMoreElements()) {
            val networkInterface = networkInterfaces.nextElement()
            if (networkInterface.isLoopback) continue

            for (interfaceAddress in networkInterface.interfaceAddresses) {
                val broadcast = interfaceAddress.broadcast
                if (broadcast != null) {
                    println("Found Broadcast IP: ${broadcast.hostAddress}")
                    broadcastingAddress = broadcast
                }
            }
        }
    }

    fun stopReceiver() =
        receiverJob.cancel()

    fun isConnected(): Boolean =
        receiverJob.isActive
}