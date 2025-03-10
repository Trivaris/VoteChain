package com.trivaris.votechain

import kotlinx.serialization.Serializable

@Serializable
data class Vote(
    val publicKeyHash: String,
    val encryptedKey: String
)