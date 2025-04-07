package com.trivaris.votechain

import java.math.BigInteger
import java.security.KeyPair
import java.security.KeyPairGenerator
import java.security.MessageDigest
import java.security.PrivateKey
import java.security.PublicKey
import java.security.Signature
import java.security.interfaces.RSAPublicKey
import java.util.Base64

object Cryptography {
    private const val KEY_SIZE = 2048
    const val ALGORITHM = "RSA"
    private const val SIGNATURE_ALGORITHM = "SHA256withRSA"

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

    fun blindData(data: String, publicKey: PublicKey, blindingFactor: BigInteger): String {
        val rsaPublic = publicKey as RSAPublicKey

        val digest = MessageDigest.getInstance("SHA-1")
        val hashBytes = digest.digest(data.toByteArray(Charsets.UTF_8))
        val m = BigInteger(1, hashBytes)

        val n = rsaPublic.modulus
        val e = rsaPublic.publicExponent

        val rPowE = blindingFactor.modPow(e, n)
        val blinded = m.multiply(rPowE).mod(n)

        return Base64.getEncoder().encodeToString(blinded.toByteArray())
    }

}