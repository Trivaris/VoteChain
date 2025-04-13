package com.trivaris.votechain.model.block

import com.trivaris.votechain.common.Config
import com.trivaris.votechain.common.Logger
import com.trivaris.votechain.model.DriverFactory
import com.trivaris.votechain.vote.Vote

object ChainInteraction {
    private lateinit var repository: BlockRepository

    fun init(driver: DriverFactory) {
        repository = BlockRepository(driver)
    }

    fun addBlock(block: BlockObject) {
        if (block.validity() != null) {
            Logger.PEER.log("Block was invalid")
            return
        }

        Logger.PEER.log("Adding valid Block")
        updateCurrentVotes(block.votes)
    }

    fun getAllBlocks(): List<BlockObject> {
        val blocks = repository.getAll()
        Logger.INFO.log("Longest Chain: ${blocks.size}")
        blocks.removeAll{ it.id == 1L }
        return blocks
    }

    fun rebuild(blocks: List<BlockObject>) {
        repository.repopulate(blocks)
    }

    fun clear() {
        repository.clear()
    }

    fun updateCurrentVotes(minedVotes: List<Vote>) {
        val currVotes = currentVotes()
        currVotes.removeAll(minedVotes)
        val newCurrVotesJson = Config.json.encodeToString(currVotes)
        repository.database.blockQueries.updateCurrentVotes(newCurrVotesJson)
    }

    private fun currentVotes(): MutableList<Vote> {
        val currentBlockQuery = repository.database.blockQueries.getById(1).executeAsOneOrNull()
        if (currentBlockQuery == null) {
            repository.insert(BlockObject(mutableListOf(), ""))
            return mutableListOf()
        }
        val block = BlockObject(currentBlockQuery)
        Logger.INFO.log("Current Votes: ${block.votes.size}")
        return block.votes
    }

    fun insertVote(vote: Vote) {
        val currVotes = currentVotes()
        currVotes.add(vote)
        val newCurrVotesJson = Config.json.encodeToString(currVotes)
        repository.database.blockQueries.updateCurrentVotes(newCurrVotesJson)
    }

    fun makeNewestBlock(): BlockObject {
        val block = BlockObject(currentVotes(), latestHash())
        block.mine()
        updateCurrentVotes(block.votes)
        return block
    }

    fun longestChain(): List<BlockObject> {
        val blocks = repository.getAll()
        val blockMap = blocks.associateBy { it.hash }
        var longestChain: List<BlockObject> = emptyList()

        blocks.forEach { block ->
            var current: BlockObject? = block
            val currentChain = mutableListOf<BlockObject>()
            val visitedIds = mutableSetOf<String>()

            // Traverse backwards until no predecessor is found or a cycle is detected.
            while (current != null && visitedIds.add(current.hash)) {
                currentChain.add(current)
                current = blockMap[current.previousHash]
            }

            // Reverse the chain so that it goes from genesis to tail.
            val orderedChain = currentChain.reversed()
            if (orderedChain.size > longestChain.size) {
                longestChain = orderedChain
            }
        }
        return longestChain
    }

    private fun latestHash(): String =
        try { longestChain().last().hash }
        catch (_: NoSuchElementException) { "" }
}