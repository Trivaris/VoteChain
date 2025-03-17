package com.trivaris.votechain.blockchain

import com.trivaris.votechain.asString
import com.trivaris.votechain.toPrivateKeyBytes
import com.trivaris.votechain.toPublicKeyBytes
import kotlinx.serialization.Serializable
import java.security.KeyPair
import java.security.PrivateKey
import java.security.PublicKey

@Serializable
data class SerializableKeyPair(val public: String, val private: String) {
    constructor(keyPair: KeyPair): this(keyPair.public.asString(), keyPair.private.asString())

    fun getKeyPair(): KeyPair =
        KeyPair(this.getPublic(), this.getPrivate())

    fun getPrivate(): PrivateKey =
        this.private.toPrivateKeyBytes()
    fun getPublic(): PublicKey =
        this.public.toPublicKeyBytes()
}