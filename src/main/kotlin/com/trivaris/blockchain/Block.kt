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

    val votes: HashMap<String, String> = Peer.currentVotes,
    val previousHash: String = if (chain.isEmpty()) "0" else chain.last().hash,
    val timestamp: Long = Date().time
) {
    init { hash = calculateHash() }

    fun calculateHash(): String {
        val toEncode = votes.toString() + previousHash + timestamp.toString() + nonce
        return toEncode.applySha256()
    }

    fun mine() {
        while (hash.substring(0..DIFFICULTY) != "0".repeat(DIFFICULTY)) {
            nonce++
            hash = calculateHash()
        }
        println("Block mined!: $hash")
    }

    fun validity(): String? {
        if (hash != calculateHash())
            return "Hashes dont match!"

        if (previousHash != chain.last().hash)
            return "Previous hashes dont match!"

        if (votes != Peer.currentVotes)
            return "Votes dont match!"

        if (timestamp < chain.last().timestamp)
            return "Block was created too late!"

        if (hash.substring(0..DIFFICULTY) != "0".repeat(DIFFICULTY))
            return "Block is not mined!"

        return null
    }
}