package com.trivaris.blockchain

import kotlinx.serialization.Serializable

@Serializable
data class Vote(
    val key: String,
    val vote: String
)