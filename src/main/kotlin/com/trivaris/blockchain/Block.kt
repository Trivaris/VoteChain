package com.trivaris.blockchain

import com.trivaris.applySha256
import kotlinx.serialization.Serializable
import java.util.Date
import com.trivaris.blockchain.Chain.it

@Serializable
data class Block(
    var hash: String = "",

    val data: String,
    val previousHash: String = if (it.isEmpty()) "0" else it.last().hash,
    val timestamp: Long = Date().time,
    val uuid: String
) {
    init { hash = calculateHash() }

    fun calculateHash(): String {
        val toEncode = data + previousHash + timestamp.toString() + uuid
        return toEncode.applySha256()
    }
}