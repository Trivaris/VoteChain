package com.trivaris.votechain.network

import com.trivaris.votechain.common.asString
import com.trivaris.votechain.common.toPrivateKey
import com.trivaris.votechain.common.toPublicKey
import kotlinx.serialization.Serializable
import java.security.KeyPair

@Serializable
data class SerializableKeyPair(val public: String, val private: String) {
    constructor(keyPair: KeyPair): this(keyPair.public.asString(), keyPair.private.asString())

    fun getKeyPair(): KeyPair =
        KeyPair(this.public.toPublicKey(), this.private.toPrivateKey())
}