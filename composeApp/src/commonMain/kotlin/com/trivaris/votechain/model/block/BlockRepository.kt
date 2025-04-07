package com.trivaris.votechain.model.block

import com.trivaris.votechain.BlockDatabase
import com.trivaris.votechain.Config
import com.trivaris.votechain.Logger
import com.trivaris.votechain.model.DriverFactory
import com.trivaris.votechain.model.Repository
import com.trivaris.votechain.voting.Vote

object BlockRepository : Repository<BlockObject> {
    override lateinit var database: BlockDatabase

    override fun init(driverFactory: DriverFactory) {
        val driver = driverFactory.createDriver()
        database = BlockDatabase(driver)
    }

    override fun insert(obj: BlockObject) {
        if (obj.validity() != null) {
            Logger.PEER.log("Block was invalid")
            return
        }

        Logger.PEER.log("Adding valid Block")
        updateCurrentVotes(obj.votes)
        database.blockQueries.insert(
            obj.hash,
            Config.json.encodeToString(obj.votes),
            obj.previousHash,
            obj.timestamp,
            obj.nonce
        )
    }

    override fun getAll(): List<BlockObject> {
        return database.blockQueries.getAll()
            .executeAsList()
            .map { row -> BlockObject(row) }
    }

    override fun clear() {
        Logger.DEBUG.log("Clearing Database")
        database.blockQueries.clear()
    }

    fun insertVote(vote: Vote) {
        val currVotes = currentVotes()
        currVotes.add(vote)
        val newCurrVotesJson = Config.json.encodeToString(currVotes)
        database.blockQueries.updateCurrentVotes(newCurrVotesJson)
    }

    fun setCurrentVotes(votes: List<Vote>) {
        votes.forEach { insertVote(it) }
    }

    fun makeNewestBlock(): BlockObject {
        val block = BlockObject(currentVotes(), latestHash())
        block.mine()
        return block
    }

    fun longestChain(): List<BlockObject> {
        val blocks = getAll().associateBy { it.hash }

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

    fun currentVotes(): MutableList<Vote> {
        val currentBlockQuery = database.blockQueries.getById(1).executeAsOneOrNull()
        val currentBlock =
            if (currentBlockQuery == null) BlockObject(mutableListOf(), "")
            else BlockObject(currentBlockQuery)

        return currentBlock.votes
    }

    private fun updateCurrentVotes(minedVotes: List<Vote>) {
        val currVotes = currentVotes()
        val newCurrVotes = currVotes.removeAll(minedVotes)
        val newCurrVotesJson = Config.json.encodeToString(newCurrVotes)
        database.blockQueries.updateCurrentVotes(newCurrVotesJson)
    }

    private fun latestHash(): String =
        try {
            longestChain().last().hash
        } catch (_: NoSuchElementException) {
            ""
        }

}