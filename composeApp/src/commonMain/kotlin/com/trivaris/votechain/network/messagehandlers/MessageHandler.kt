package com.trivaris.votechain.network.messagehandlers

import com.trivaris.votechain.network.MessageEnvelope

interface MessageHandler {
    fun incoming(envelope: MessageEnvelope)
    fun outgoing(envelope: MessageEnvelope)
}