package com.trivaris.votechain.blockchain

import com.trivaris.votechain.DIFFICULTY
import com.trivaris.votechain.Vote
import com.trivaris.votechain.applySha256
import kotlinx.serialization.Serializable
import java.util.Date

@Serializable
open class Block(
    private var hash: String = "",
    var nonce: Int = 0,

    val votes: MutableList<Vote> = mutableListOf(),
    val previousHash: String,
    val timestamp: Long = Date().time
) {

    init { hash = calculateHash() }

    fun calculateHash(): String {
        val toEncode = votes.toString() + previousHash + timestamp.toString() + nonce
        return toEncode.applySha256()
    }

    fun mine() {
        hash = calculateHash()
        while (true) {
            if (hash.substring(0..<DIFFICULTY) == "0".repeat(DIFFICULTY)) break
            nonce++
            hash = calculateHash()
            println(nonce)
        }
        println("Block mined!: $hash")
    }

    fun validity(): String? {
        if (hash != calculateHash())
            return "Hashes dont match!"

        if (hash.substring(0..DIFFICULTY) != "0".repeat(DIFFICULTY))
            return "Block is not mined!"

        return null
    }
}