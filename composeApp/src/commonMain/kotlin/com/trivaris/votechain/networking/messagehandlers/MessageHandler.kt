package com.trivaris.votechain.networking.messagehandlers

import com.trivaris.votechain.networking.MessageEnvelope

interface MessageHandler {
    fun incoming(envelope: MessageEnvelope)
    fun outgoing(envelope: MessageEnvelope)
}