package com.trivaris.votechain.blockchain

import com.trivaris.votechain.voting.VotingManager

object BlockManager {
    private var chain = BlockStorage
    private var votingManager = VotingManager

    fun makeNewestBlock(): Block {
        val block = Block(votingManager.currentVotes.toMutableMap(), chain.latestHash())
        votingManager.currentVotes.clear()
        return block.apply { mine() }
    }

    fun newBlock(block: Block) {
        if (block.validity() == null) {
            println("Adding valid Block")
            chain.add(block)
        }
        else println("Block was invalid")
    }
}