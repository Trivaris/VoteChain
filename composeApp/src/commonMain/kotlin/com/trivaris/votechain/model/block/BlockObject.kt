package com.trivaris.votechain.model.block

import com.trivaris.votechain.Block
import com.trivaris.votechain.common.Config
import com.trivaris.votechain.common.Logger
import com.trivaris.votechain.common.applySha256
import com.trivaris.votechain.model.Object
import com.trivaris.votechain.vote.Vote
import kotlinx.serialization.Serializable
import java.util.Date

@Serializable
data class BlockObject (
    val votes: MutableList<Vote>,
    val previousHash: String,
    val timestamp: Long = Date().time,

    var nonce: Long = 0,
    val id: Long? = null
): Object {
    constructor(block: Block) : this(
        id = null,
        votes = Config.json.decodeFromString(block.votes),
        previousHash = block.previous_hash,
        timestamp = block.timestamp,
        nonce = block.nonce
    )
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