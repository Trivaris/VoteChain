package com.trivaris.votechain

import io.ktor.server.application.ApplicationCall
import io.ktor.server.plugins.origin
import io.ktor.websocket.Frame.Text
import java.security.MessageDigest
import java.security.PrivateKey
import java.security.PublicKey
import javax.crypto.Cipher
import java.util.Base64

const val DIFFICULTY = 2

fun main() {

}

//TODO: Make this more readable
enum class Connection {
    CONNECT,
    DISCONNECT,

    PEER_LIST,
    PEER_JOINED,
    PEER_LEFT,
    CONNECT_REQUEST,
    CONNECT_INFO,
    INVALID_REQUEST;

    fun withMessage(message: String): Text {
        return Text("${this.name}:$message")
    }

}

fun String.applySha256(): String {
    val digest = MessageDigest.getInstance("SHA-256")
    val hash = digest.digest(this.toByteArray(Charsets.UTF_8))
    return hash.joinToString("") { String.format("%02x", it) }
}

val ApplicationCall.ip: String
    get() = this.request.origin.remoteAddress

val ApplicationCall.port: Int
    get() = this.request.origin.remotePort

fun String.isType(prefix: Connection): Boolean {
    return this.startsWith(prefix.name)
}
fun String.getData(type: Connection): String {
    return this.removePrefix("${type.name}:")
}

fun <T> MutableList<T>.isSupersetOf(other: MutableList<T>): Boolean {
    return other.all { it in this }
}

fun encrypt(publicKey: PublicKey, data: String): String {
    val cipher = Cipher.getInstance("RSA")
    cipher.init(Cipher.ENCRYPT_MODE, publicKey)
    val encryptedBytes = cipher.doFinal(data.toByteArray())
    return Base64.getEncoder().encodeToString(encryptedBytes)
}

fun decrypt(privateKey: PrivateKey, encryptedData: String): String {
    val cipher = Cipher.getInstance("RSA")
    cipher.init(Cipher.DECRYPT_MODE, privateKey)
    val encryptedBytes = Base64.getDecoder().decode(encryptedData)
    val decryptedBytes = cipher.doFinal(encryptedBytes)
    return String(decryptedBytes)
}