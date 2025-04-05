package com.trivaris.votechain.blockchain

import com.trivaris.votechain.Logger
import kotlinx.serialization.json.Json
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.deleteAll
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction

object BlockStorage {

    fun add(block: Block) {
        Logger.INFO.log("Saving block: ${block.hash}")
        transaction {
            BlocksTable.deleteWhere { hash eq block.hash }
            BlocksTable.insert {
                it[hash] = block.hash
                it[previousHash] = block.previousHash
                it[timestamp] = block.timestamp
                it[nonce] = block.nonce
                it[votes] = Json.encodeToString(block.votes)
            }
        }
    }

    fun clear() {
        transaction {
            BlocksTable.deleteAll()
        }
    }

    fun getBlocks(): Map<String, Block> {
        val blocks = mutableMapOf<String, Block>()
        transaction {
            BlocksTable.selectAll().forEach { row ->
                val votesJson = row[BlocksTable.votes]
                val votes: MutableMap<String, String> = Json.decodeFromString(votesJson)
                val previousHash = row[BlocksTable.previousHash]
                val timestamp = row[BlocksTable.timestamp]
                val nonce = row[BlocksTable.nonce]
                val block = Block(votes, previousHash, timestamp, nonce)
                blocks[block.hash] = block
            }
        }
        return blocks
    }

    fun longestChain(): List<Block> {
        val blocks = getBlocks()

        val allHashes = blocks.keys.toMutableSet()
        val parentHashes = blocks.values.map { it.previousHash }.toSet()
        allHashes.removeAll(parentHashes)

        val endpointBlocks = allHashes.mapNotNull { blocks[it] }

        val chains = mutableListOf<List<Block>>()
        endpointBlocks.forEach { block ->
            val chain = mutableListOf<Block>()
            var currentBlock: Block? = block
            while (currentBlock != null && currentBlock.previousHash.isNotEmpty()) {
                chain.add(0, currentBlock)
                currentBlock = blocks[currentBlock.previousHash]
            }
            currentBlock?.let { chain.add(0, it) }
            chains.add(chain)
        }

        return chains.maxByOrNull { it.size } ?: emptyList()
    }

    fun setBlocks(newBlocks: Map<String, Block>) {
        clear()
        newBlocks.values.forEach { add(it) }
    }

    fun latestHash(): String =
        try { longestChain().last().hash } catch (e: Exception) { "" }
}