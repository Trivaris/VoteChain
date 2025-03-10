package com.trivaris.votechain.peer.networking
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.net.DatagramPacket
import java.net.DatagramSocket
import java.net.InetAddress
import java.net.NetworkInterface

const val BROADCAST_PORT = 12345
const val BROADCAST_IP = "255.255.255.255"
const val BUFFER_SIZE = 1024

object Dispatcher {
    val socket = DatagramSocket()
    val localIp: String = InetAddress.getLocalHost().hostAddress
    val localPort: Int = socket.localPort


    fun main() {
        println(localPort)
        println(localIp)
    }

    @OptIn(DelicateCoroutinesApi::class)
    fun joinNetwork() {
        val message = "$localIp:$localPort".toByteArray()
        sendBroadcastMessage(message)

        GlobalScope.launch {
            receiveData()
        }
    }

    fun leaveNetwork() {
        val message = "BYE".toByteArray()
        sendBroadcastMessage(message)
    }

    fun sendBroadcastMessage(message: ByteArray) {
        val buffer = ByteArray(BUFFER_SIZE)
        System.arraycopy(message, 0, buffer, 0, message.size)

        val broadcastAddress = InetAddress.getByName(BROADCAST_IP)
        val packet = DatagramPacket(buffer, buffer.size, broadcastAddress, BROADCAST_PORT)

        socket.send(packet)
    }

    suspend fun receiveData() {
        val buffer = ByteArray(BUFFER_SIZE)
        val packet = DatagramPacket(buffer, buffer.size)

        withContext(Dispatchers.IO) {
            while (true) {
                socket.receive(packet)
                val message = String(packet.data, 0, packet.length)
                println("Received: $message from ${packet.address.hostAddress}")
            }
        }
    }

    fun getBroadcastAddress(): String? {
        val networkInterfaces = NetworkInterface.getNetworkInterfaces()

        while (networkInterfaces.hasMoreElements()) {
            val networkInterface = networkInterfaces.nextElement()
            if (networkInterface.isLoopback) continue

            for (interfaceAddress in networkInterface.interfaceAddresses) {
                val broadcast = interfaceAddress.broadcast

                if (broadcast == null) continue
                else return broadcast.hostAddress
            }
        }
        return null
    }

}