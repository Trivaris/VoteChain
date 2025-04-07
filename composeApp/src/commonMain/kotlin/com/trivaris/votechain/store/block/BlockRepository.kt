package com.trivaris.votechain.store.block

import com.trivaris.votechain.BlockDatabase
import com.trivaris.votechain.Config
import com.trivaris.votechain.Logger
import com.trivaris.votechain.store.DriverFactory
import com.trivaris.votechain.voting.VotingManager

object BlockRepository {
    private lateinit var database: BlockDatabase

    fun makeNewestBlock(): BlockObject =
        BlockObject(VotingManager.getCurrentVotes(), latestHash()).apply { mine() }

    fun addBlock(block: BlockObject) {
        if (block.validity() == null) {
            Logger.PEER.log("Adding valid Block")
            VotingManager.removeFromCurrentVotes(block.votes)
            insert(block)
        } else Logger.PEER.log("Block was invalid")
    }

    fun getBlocks(): Map<String, BlockObject> =
        database.blockQueries.getAll()
            .executeAsList()
            .associate { row -> row.hash to BlockObject(row) }

    fun setBlocks(blocks: List<BlockObject>) {
        clear()
        blocks.forEach { block ->
            insert(block)
        }
    }

    private fun latestHash(): String =
        try {
            longestChain().last().hash
        } catch (_: NoSuchElementException) {
            ""
        }

    fun longestChain(): List<BlockObject> {
        val blocks = getBlocks()

        val allHashes = blocks.keys.toMutableSet()
        val parentHashes = blocks.values.map { it.previousHash }.toSet()
        allHashes.removeAll(parentHashes)

        val endpointBlocks = allHashes.mapNotNull { blocks[it] }

        val chains = mutableListOf<List<BlockObject>>()
        endpointBlocks.forEach { block ->
            val chain = mutableListOf<BlockObject>()
            var currentBlock: BlockObject? = block
            while (currentBlock != null && currentBlock.previousHash.isNotEmpty()) {
                chain.add(0, currentBlock)
                currentBlock = blocks[currentBlock.previousHash]
            }
            currentBlock?.let { chain.add(0, it) }
            chains.add(chain)
        }

        return chains.maxByOrNull { it.size } ?: emptyList()
    }

    fun clear() {
        Logger.DEBUG.log("Clearing Database")
        database.blockQueries.clear()
    }

    private fun insert(block: BlockObject) =
        database.blockQueries.insert(
            block.hash,
            Config.json.encodeToString(block.votes),
            block.previousHash,
            block.timestamp,
            block.nonce
        )

    fun initDatabase(driverFactory: DriverFactory){
        val driver = driverFactory.createDriver()
        database = BlockDatabase(driver)
    }
}