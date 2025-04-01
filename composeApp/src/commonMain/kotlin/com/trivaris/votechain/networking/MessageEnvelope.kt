package com.trivaris.votechain.networking

import com.trivaris.votechain.Config
import kotlinx.serialization.Serializable

@Serializable
data class MessageEnvelope(
    val message: Message,
    val recipient: String = Config.data.serverIP,
    var originator: String = NetworkManager.ownAddress,
    val previousRecipients: MutableList<String>? = null
)
