package com.trivaris.votechain.network

import com.trivaris.votechain.common.Config
import kotlinx.serialization.Serializable

@Serializable
data class MessageEnvelope(
    val message: Message,
    val recipient: String = Config.data.serverIP,
    var originator: String = NetworkManager.ownAddress,
    val previousRecipients: MutableList<String>? = null
)
