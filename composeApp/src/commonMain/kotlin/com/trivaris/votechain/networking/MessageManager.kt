package com.trivaris.votechain.networking

object MessageManager {

    fun outgoing(message: Message) =
        outgoing(MessageEnvelope(message))
    fun outgoing(envelope: MessageEnvelope) {
        val handler = envelope.message.type.clazz.constructors.first().call()
        handler.outgoing(envelope)
    }

    fun incoming(envelope: MessageEnvelope) {
        val handler = envelope.message.type.clazz.constructors.first().call()
        handler.incoming(envelope)
    }
}