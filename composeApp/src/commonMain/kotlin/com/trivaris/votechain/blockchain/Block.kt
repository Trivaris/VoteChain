package com.trivaris.votechain.blockchain

import com.trivaris.votechain.Config
import com.trivaris.votechain.applySha256
import kotlinx.serialization.Serializable
import java.util.Date

@Serializable
open class Block(
    val votes: MutableMap<String, String>,
    val previousHash: String,

    val timestamp: Long = Date().time,
    var nonce: Int = 0,
) {
    val hash: String
        get() = calculateHash()

    private fun calculateHash(): String {
        val toEncode = votes.toString() + previousHash + timestamp.toString() + nonce
        return toEncode.applySha256()
    }

    fun mine() {
        while (true) {
            if (hash.substring(0..<Config.data.difficulty) == "0".repeat(Config.data.difficulty))
                break
            nonce++
            if (Config.data.printHashCalc)
                println(nonce)
        }
        println("Block mined!: $hash")
    }

    fun validity(): String? {
        if (hash.substring(0..<Config.data.difficulty) != "0".repeat(Config.data.difficulty))
            return "Block is not mined!"
        return null
    }
}