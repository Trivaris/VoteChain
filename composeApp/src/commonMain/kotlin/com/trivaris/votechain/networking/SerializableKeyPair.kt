package com.trivaris.votechain.networking

import com.trivaris.votechain.asString
import com.trivaris.votechain.toPrivateKey
import com.trivaris.votechain.toPublicKey
import kotlinx.serialization.Serializable
import java.security.KeyPair

@Serializable
data class SerializableKeyPair(val public: String, val private: String) {
    constructor(keyPair: KeyPair): this(keyPair.public.asString(), keyPair.private.asString())

    fun getKeyPair(): KeyPair =
        KeyPair(this.public.toPublicKey(), this.private.toPrivateKey())
}