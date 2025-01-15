package com.trivaris.blockchain

import com.trivaris.blockchain.Chain.it as chain
import com.trivaris.networking.Dispatcher.post
import com.trivaris.badRequestThreshold
import io.ktor.http.HttpStatusCode

object Validity {

    suspend fun checkNetwork(peers: IntRange, block: Block, add: Boolean, origin: Int): Boolean {
        var badRequests = 0
        for (peer in peers) {
            if (peer == Chain.userIP() || peer == origin) continue
            else if (post(block, peer, if (add) "add" else "probe").status == HttpStatusCode.BadRequest) badRequests++
        }
        return badRequests < badRequestThreshold
    }

    fun checkLocal(block: Block): Boolean =
        Hashes.withLast(block) && !alreadyVoted(block) && correctTime(block)

    fun alreadyVoted(uuid: String): Boolean =
        Chain.voterUUIDs().contains(uuid)
    fun alreadyVoted(block: Block): Boolean =
        alreadyVoted(block.uuid)

    private fun correctTime(block: Block): Boolean =
        block.timestamp > (chain.lastOrNull()?.timestamp ?: 0)

    private object Hashes {
        fun current(block: Block): Boolean =
            block.hash == block.calculateHash()

        fun previous(curr: Block, prev: Block?): Boolean =
            if (prev == null) true else prev.hash == curr.previousHash

        fun check(curr: Block, prev: Block?): Boolean =
            current(curr) && previous(curr, prev)

        fun withLast(block: Block): Boolean =
            check(block, chain.lastOrNull())
    }

}