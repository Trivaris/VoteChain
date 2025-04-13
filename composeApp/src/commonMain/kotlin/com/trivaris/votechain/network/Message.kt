package com.trivaris.votechain.network

import com.trivaris.votechain.model.block.BlockObject
import com.trivaris.votechain.network.messagehandlers.MessageType
import com.trivaris.votechain.vote.Vote
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlin.String

@Serializable
data class Message(
    val type: MessageType,
    val data: String = ""
) {

    constructor(vote: Vote) : this(
        type = MessageType.VOTE,
        data = Json.encodeToString(vote)
    )

    constructor(block: BlockObject) : this(
        type = MessageType.BLOCK,
        data = Json.encodeToString(block)
    )
}