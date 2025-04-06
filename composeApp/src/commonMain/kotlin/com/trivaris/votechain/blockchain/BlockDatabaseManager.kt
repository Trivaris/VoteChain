package com.trivaris.votechain.blockchain

import com.trivaris.votechain.BlockDatabase
import com.trivaris.votechain.Logger
import com.trivaris.votechain.blockchain.database.BlockObject
import com.trivaris.votechain.blockchain.database.DriverFactory
import com.trivaris.votechain.blockchain.database.dropAll
import com.trivaris.votechain.blockchain.database.getAllBlocks
import com.trivaris.votechain.blockchain.database.insertBlock
import com.trivaris.votechain.voting.VotingManager

object BlockDatabaseManager {
    private lateinit var database: BlockDatabase

    fun makeNewestBlock(): BlockObject =
        BlockObject(VotingManager.getCurrentVotes(), latestHash()).apply { mine() }

    fun newBlock(block: BlockObject) {
        if (block.validity() == null) {
            Logger.PEER.log("Adding valid Block")
            VotingManager.removeFromCurrentVotes(block.votes)
            addBlock(block)
        }
        else Logger.PEER.log("Block was invalid")
    }

    fun getBlocks(): Map<String, BlockObject> =
        database.getAllBlocks()

    fun setBlocks(blocks: List<BlockObject>) {
        database.dropAll()
        blocks.forEach { block ->
            database.insertBlock(block)
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

    fun clearDatabase() {
        Logger.DEBUG.log("Clearing Database")
        database.dropAll()
    }

    private fun addBlock(block: BlockObject) =
        database.insertBlock(block)

    fun initDatabase(driverFactory: DriverFactory){
        val driver = driverFactory.createDriver()
        database = BlockDatabase(driver)
    }
}