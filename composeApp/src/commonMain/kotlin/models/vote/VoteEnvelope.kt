package com.trivaris.votechain.models.vote

import com.trivaris.votechain.models.candidate.CandidateObject
import kotlinx.serialization.Serializable
import kotlin.random.Random

@Serializable
data class VoteEnvelope(
    val encryptedVote: String,
    val signature: String,
    val nonce: Int = Random.nextInt()
)

@Serializable
data class Vote(
    val candidateId: String,
)

@Serializable
data class VoteResult(
    val candidateId: String,
    var count: Int
)