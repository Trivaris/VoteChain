package com.trivaris.votechain.blockchain.database

import com.trivaris.votechain.Config
import com.trivaris.votechain.Logger
import com.trivaris.votechain.applySha256
import kotlinx.serialization.Serializable
import java.util.Date

@Serializable
open class BlockObject(
    val votes: MutableMap<String, String>,
    val previousHash: String,

    val timestamp: Long = Date().time,
    var nonce: Long = 0,
) {
    val hash: String
        get() = Config.json.encodeToString(this).applySha256()

    fun mine() {
        while (true) {
            if (hash.substring(0..<Config.data.difficulty) == "0".repeat(Config.data.difficulty))
                break
            nonce++
            if (Config.data.printHashCalc)
                Logger.INFO.log(nonce)
        }
        Logger.INFO.log("Block mined: $hash")
    }

    fun validity(): String? {
        if (hash.substring(0..<Config.data.difficulty) != "0".repeat(Config.data.difficulty))
            return "Block is not mined!"
        return null
    }
}