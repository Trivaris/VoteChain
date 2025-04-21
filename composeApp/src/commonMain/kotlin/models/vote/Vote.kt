package com.trivaris.votechain.models.vote

import kotlinx.serialization.Serializable

@Serializable
data class Vote(
    val encryptedVote: String,
    val signature: String
)

@Serializable
data class VoteResult(
    val candidateId: String,
    val count: Int
)