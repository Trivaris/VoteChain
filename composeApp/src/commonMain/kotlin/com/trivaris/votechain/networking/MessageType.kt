package com.trivaris.votechain.networking

enum class MessageType {
    JOIN_NETWORK,
    LEAVE_NETWORK,
    VOTE,
    BLOCK,
    KEYS_REQUEST,
    KEYS_RESPONSE,
    INVALID;
}