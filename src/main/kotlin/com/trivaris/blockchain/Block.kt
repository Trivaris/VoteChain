package com.trivaris.blockchain

import com.trivaris.networking.applySha256
import kotlinx.serialization.Serializable
import java.util.Date

@Serializable
data class Block(
    var hash: String = "",

    val data: String,
    val previousHash: String = Chain.it.last().hash,
    val timestamp: Long = Date().time,
    val hashedIP: String
) {
    init { hash = calculateHash() }

    fun calculateHash(): String {
        val toEncode = data + previousHash + timestamp.toString() + hashedIP
        return toEncode.applySha256()
    }
}