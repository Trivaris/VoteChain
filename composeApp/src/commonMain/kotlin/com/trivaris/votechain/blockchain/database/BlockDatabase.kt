package com.trivaris.votechain.blockchain.database

import com.trivaris.votechain.Block
import com.trivaris.votechain.BlockDatabase
import kotlinx.serialization.json.Json

val json = Json() { encodeDefaults = true }

fun BlockDatabase.insertBlock(block: BlockObject) {
    this.blockQueries.insertBlock(
        block.hash,
        json.encodeToString(block.votes),
        block.previousHash,
        block.timestamp,
        block.nonce
    )
}

fun BlockDatabase.dropAll() {
    this.blockQueries.dropAll()
}

fun Block.toBlockObject(): BlockObject {
    val votesMap: MutableMap<String, String> = Json.decodeFromString(this.votes)
    return BlockObject(
        votes = votesMap,
        previousHash = this.previous_hash,
        timestamp = this.timestamp,
        nonce = this.nonce
    )
}

fun BlockDatabase.getAllBlocks(): Map<String, BlockObject> {
    return blockQueries.selectAll()
        .executeAsList()
        .associate { row -> row.hash to row.toBlockObject() }
}

