package com.trivaris.votechain.blockchain

object BlockStorage {
    // Hash -> Block
    private val blocks: MutableMap<String, Block> = mutableMapOf()
    // Hash -> Hashes of Children
    private val adjacencyList: MutableMap<String, MutableList<String>> = mutableMapOf()

    fun add(block: Block) {
        val hash = block.hash

        blocks.put(hash, block)
        adjacencyList.putIfAbsent(hash, mutableListOf())

        block.previousHash.let { prev ->
            adjacencyList.putIfAbsent(prev, mutableListOf())
            adjacencyList[prev]?.add(hash)
        }
    }

    fun latestHash(): String =
        try { longestChain().last().hash } catch (_: Exception) { "" }
    fun getBlocks() = blocks

    fun longestChain(): List<Block> {
        val visited = mutableSetOf<String>()
        var longestChain = emptyList<Block>()

        for (block in blocks.values) {
            if (block.hash !in visited) {
                val chain = dfs(block.hash, visited)
                if (chain.size > longestChain.size) {
                    longestChain = chain
                }
            }
        }
        return longestChain
    }

    private fun dfs(startHash: String, visited: MutableSet<String>): List<Block> {
        val stack = mutableListOf<String>()
        val chain = mutableListOf<String>()
        stack.add(startHash)

        while (stack.isNotEmpty()) {
            val current = stack.removeAt(stack.size - 1)
            if (current !in visited) {
                visited.add(current)
                chain.add(current)

                adjacencyList[current]?.let { nextNodes ->
                    for (next in nextNodes) {
                        if (next !in visited) {
                            stack.add(next)
                        }
                    }
                }
            }
        }
        return chain.mapNotNull { blocks[it] }
    }
}