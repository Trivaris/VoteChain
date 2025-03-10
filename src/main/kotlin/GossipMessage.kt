package com.trivaris.votechain

import java.util.Date

data class GossipMessage(
    val timestamp: Long = Date().time,
    val vote: Vote,
    val received: MutableList<String>
)