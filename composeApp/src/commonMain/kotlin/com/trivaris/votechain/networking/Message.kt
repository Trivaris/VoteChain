package com.trivaris.votechain.networking

import com.trivaris.votechain.blockchain.Block
import com.trivaris.votechain.voting.SerializableVote
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlin.String

@Serializable
data class Message(val type: MessageType, var originator: String = NetworkManager.ownAddress, val data: String = "", val length: Int = data.length) {
    val raw: String by
        lazy { "${data.length}::$type::$originator::$data" }

    fun toByteArray(): ByteArray =
        raw.toByteArray()

    constructor(parts: List<String>) : this(
        type        = MessageType.fromString(parts[1]),
        originator  = parts[2],
        data        = parts[3].substring(0..<parts[0].toInt()),
        length      = parts[0].toInt() )

    constructor(vote: SerializableVote, originator: String = NetworkManager.ownAddress) : this(
        type = MessageType.VOTE,
        originator = originator,
        data = Json.encodeToString(vote) )
    constructor(block: Block, originator: String = NetworkManager.ownAddress) : this(
        type = MessageType.BLOCK,
        originator = originator,
        data = Json.encodeToString(block) )

    constructor(raw: String)            : this(raw.split("::"))
    constructor(raw: ByteArray)         : this(raw.decodeToString())
}

enum class MessageType {
    JOIN_NETWORK,
    LEAVE_NETWORK,
    VOTE,
    BLOCK,
    KEYS_REQUEST,
    INVALID;

    companion object {
        fun fromString(raw: String): MessageType =
            MessageType.entries.find { raw.startsWith(it.name) } ?: INVALID
    }
}