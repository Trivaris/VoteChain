package com.trivaris.votechain.networking

import com.trivaris.votechain.blockchain.database.BlockObject
import com.trivaris.votechain.networking.messagehandlers.MessageType
import com.trivaris.votechain.voting.SerializableVote
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlin.String

@Serializable
data class Message(
    val type: MessageType,
    val data: String = ""
) {

    constructor(vote: SerializableVote) : this(
        type = MessageType.VOTE,
        data = Json.encodeToString(vote)
    )

    constructor(block: BlockObject) : this(
        type = MessageType.BLOCK,
        data = Json.encodeToString(block)
    )
}