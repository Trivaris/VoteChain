package com.trivaris.votechain.network.messagehandlers

import kotlin.reflect.KClass

enum class MessageType(val clazz: KClass<out MessageHandler>) {
    JOIN_REQUEST(JoinRequestMessageHandler::class),
    JOIN_RESPONSE(JoinResponseMessageHandler::class),
    NEW_PARTICIPANT(NewParticipantMessageHandler::class),
    LEAVE_NETWORK(LeaveNetworkMessageHandler::class),
    VOTE(VoteMessageHandler::class),
    BLOCK(BlockMessageHandler::class),
    KEYS_REQUEST(KeysRequestMessageHandler::class),
    KEYS_RESPONSE(KeysResponseMessageHandler::class);
}