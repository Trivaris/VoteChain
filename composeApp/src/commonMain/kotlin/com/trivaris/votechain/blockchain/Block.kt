package com.trivaris.votechain.blockchain

import com.trivaris.votechain.config
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

    fun calculateHash(): String {
        val toEncode = votes.toString() + previousHash + timestamp.toString() + nonce
        return toEncode.applySha256()
    }

    fun mine() {
        while (true) {
            if (hash.substring(0..<config!!.data.difficulty) == "0".repeat(config!!.data.difficulty))
                break
            nonce++
            if (config!!.data.printHashCalc)
                println(nonce)
        }
        println("Block mined!: $hash")
    }

    fun validity(): String? {
        if (hash.substring(0..<config!!.data.difficulty) != "0".repeat(config!!.data.difficulty))
            return "Block is not mined!"
        return null
    }
}