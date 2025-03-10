package com.trivaris.votechain.blockchain

import com.trivaris.votechain.Vote

class Chain {
    private val blocks: MutableMap<String, Block> = mutableMapOf()
    private val adjacencyList: MutableMap<String, MutableList<String>> = mutableMapOf()
    val votes: MutableList<Vote>
        get() = blocks.values.flatMap { it.votes }.toMutableList()

    operator fun plusAssign(block: Block) {
        blocks += Pair(block.calculateHash(), block)
        adjacencyList.putIfAbsent(block.calculateHash(), mutableListOf())

        block.previousHash.let { prev ->
            adjacencyList.putIfAbsent(prev, mutableListOf())
            adjacencyList[prev]?.add(block.calculateHash())
        }
    }

    fun minusAssign(block: Block) {
        blocks -= block.calculateHash()
        adjacencyList.remove(block.calculateHash())
        adjacencyList.values.forEach { it.remove(block.calculateHash()) }
    }

    fun print() {
        adjacencyList.forEach { (node, neighbors) ->
            println("$node -> $neighbors")
        }
    }
}
