package com.trivaris.votechain.blockchain

import com.trivaris.votechain.Logger
import com.trivaris.votechain.voting.VotingManager

object BlockManager {

    fun makeNewestBlock(): Block {
        val block = Block(VotingManager.getCurrentVotes(), BlockStorage.latestHash())
        return block.apply { mine() }
    }

    fun newBlock(block: Block) {
        if (block.validity() == null) {
            Logger.PEER.log("Adding valid Block")
            VotingManager.removeFromCurrentVotes(block.votes)
            BlockStorage.add(block)
        }
        else Logger.PEER.log("Block was invalid")
    }
}