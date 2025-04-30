package com.trivaris.votechain.network

interface GossipService {
    suspend fun gossip(digest: Map<String, Long>): Map<String, Long>
}