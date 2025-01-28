package com.trivaris.blockchain

import com.trivaris.applySha256
import kotlinx.serialization.Serializable
import java.util.Date
import com.trivaris.blockchain.Peer.chain
const val DIFFICULTY = 5

@Serializable
data class Block(
    var hash: String = "",
    var nonce: Int = 0,

    val votes: HashMap<String, String>,
    val previousHash: String = if (chain.isEmpty()) "0" else chain.last().hash,
    val timestamp: Long = Date().time,
    val uuid: String
) {
    init { hash = calculateHash() }

    fun calculateHash(): String {
        val toEncode = votes.toString() + previousHash + timestamp.toString() + uuid + nonce
        return toEncode.applySha256()
    }

    fun mine(): Boolean {
        while (hash.substring(0..5) != "0".repeat(DIFFICULTY)) {
            nonce++
            hash = calculateHash()
        }
        return true
    }

    fun validity(): String? {
        if (hash != calculateHash()) return "Hashes dont match!"
        if (previousHash != chain.last().hash) return "Previous hashes dont match!"
        if (votes != Peer.currentVotes) return "Votes dont match"
        if (timestamp < chain.last().timestamp) return "Block was created too late!"

        return null
    }
}