package com.trivaris.blockchain

import kotlinx.serialization.Serializable

@Serializable
data class Pair(
    val first: String,
    val second: String
)