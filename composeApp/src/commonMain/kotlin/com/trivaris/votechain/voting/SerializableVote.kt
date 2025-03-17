package com.trivaris.votechain.voting

import kotlinx.serialization.Serializable

@Serializable
data class SerializableVote(
    val publicKeyStringHash: String,
    val candidateSignature: String
)