package com.trivaris.votechain.blockchain.database

import com.trivaris.votechain.Block
import com.trivaris.votechain.BlockDatabase
import com.trivaris.votechain.Config

fun BlockDatabase.insertBlock(block: BlockObject) {
    this.blockQueries.insertBlock(
        block.hash,
        Config.json.encodeToString(block.votes),
        block.previousHash,
        block.timestamp,
        block.nonce
    )
}

fun BlockDatabase.dropAll() {
    this.blockQueries.dropAll()
}

fun Block.toBlockObject(): BlockObject {
    val votesMap: MutableMap<String, String> = Config.json.decodeFromString(this.votes)
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

