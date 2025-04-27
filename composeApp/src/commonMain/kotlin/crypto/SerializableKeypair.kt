package com.trivaris.votechain.crypto

import kotlinx.serialization.Serializable
import java.security.Key
import java.security.KeyFactory
import java.security.PrivateKey
import java.security.PublicKey
import java.security.spec.PKCS8EncodedKeySpec
import java.security.spec.X509EncodedKeySpec
import java.util.Base64

@Serializable
data class SerializableKeypair(
    val public: String?,
    val private: String?
) {
    constructor(
        publicKey: PublicKey,
        privateKey: PrivateKey
    ) : this(publicKey.encodeToJson(), privateKey.encodeToJson())

    fun getPublic(): PublicKey {
        val decoded = Base64.getDecoder().decode(public)
        val spec = X509EncodedKeySpec(decoded)
        return KeyFactory.getInstance("RSA").generatePublic(spec)
    }

    fun getPrivate(): PrivateKey {
        val decoded = Base64.getDecoder().decode(private)
        val spec = PKCS8EncodedKeySpec(decoded)
        return KeyFactory.getInstance("RSA").generatePrivate(spec)
    }
}

fun Key.encodeToJson(): String {
    return Base64.getEncoder().encodeToString(this.encoded)
}