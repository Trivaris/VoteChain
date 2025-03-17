package com.trivaris.votechain

import java.security.KeyPair
import java.security.KeyPairGenerator
import java.security.PrivateKey
import java.security.PublicKey
import java.security.Signature
import java.util.Base64

object Cryptography {
    const val KEY_SIZE = 2048
    const val ALGORITHM = "RSA"
    const val SIGNATURE_ALGORITHM = "SHA256withRSA"

    fun generateKeyPair(): KeyPair {
        val keyGen = KeyPairGenerator.getInstance(ALGORITHM)
        keyGen.initialize(KEY_SIZE)
        return keyGen.generateKeyPair()
    }

    fun signData(data: String, privateKey: PrivateKey): String {
        val signature = Signature.getInstance(SIGNATURE_ALGORITHM)
        signature.initSign(privateKey)
        signature.update(data.toByteArray())
        val signedBytes = signature.sign()
        return Base64.getEncoder().encodeToString(signedBytes)
    }

    fun verifySignature(data: String, signatureStr: String, publicKey: PublicKey): Boolean {
        val signature = Signature.getInstance(SIGNATURE_ALGORITHM)
        signature.initVerify(publicKey)
        signature.update(data.toByteArray())
        val signedBytes = Base64.getDecoder().decode(signatureStr)
        return signature.verify(signedBytes)
    }

}