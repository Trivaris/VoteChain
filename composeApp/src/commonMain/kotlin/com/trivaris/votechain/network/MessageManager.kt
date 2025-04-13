package com.trivaris.votechain.network

import com.trivaris.votechain.network.messagehandlers.MessageHandler

object MessageManager {
    fun outgoing(message: Message) =
        outgoing(MessageEnvelope(message))
    fun outgoing(envelope: MessageEnvelope) {
        val handler = getHandler(envelope)
        handler.outgoing(envelope)
    }

    fun incoming(envelope: MessageEnvelope) {
        val handler = getHandler(envelope)
        handler.incoming(envelope)
    }

    private fun getHandler(envelope: MessageEnvelope): MessageHandler {
        val handlerContructor = envelope.message.type.clazz.constructors.first()
        return handlerContructor.call()
    }
}