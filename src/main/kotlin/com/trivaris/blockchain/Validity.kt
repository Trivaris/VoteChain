package com.trivaris.blockchain

import com.trivaris.blockchain.Chain.it as chain

object Validity {

    fun checkAsNewest(block: Block): Boolean =
        withLatest(block) && !alreadyVoted(block)

    private fun hashes(curr: Block, prev: Block): Boolean =
        currentHashes(curr) && previousHashes(curr, prev)

    private fun withLatest(block: Block): Boolean =
        hashes(block, chain.last())

    private fun currentHashes(block: Block): Boolean =
        block.hash == block.calculateHash()

    private fun previousHashes(curr: Block, prev: Block): Boolean =
        prev.hash == curr.previousHash

    private fun alreadyVoted(block: Block): Boolean =
        Chain.getVoters().contains(block.hashedIP)

}