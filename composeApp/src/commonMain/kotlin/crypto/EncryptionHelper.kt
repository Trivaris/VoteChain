package com.trivaris.votechain.crypto

import java.security.PrivateKey
import java.security.PublicKey
import java.security.Signature
import java.util.Base64
import javax.crypto.Cipher

class EncryptionHelper {

    fun encrypt(plainText: String, publicKey: PublicKey): String {
        val cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding")
        cipher.init(Cipher.ENCRYPT_MODE, publicKey)
        val encryptedBytes = cipher.doFinal(plainText.toByteArray(Charsets.UTF_8))
        return Base64.getEncoder().encodeToString(encryptedBytes)
    }

    fun decrypt(encryptedText: String, privateKey: PrivateKey): String {
        val cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding")
        cipher.init(Cipher.DECRYPT_MODE, privateKey)
        val decryptedBytes = cipher.doFinal(Base64.getDecoder().decode(encryptedText))
        return String(decryptedBytes, Charsets.UTF_8)
    }

    fun verifySignature(signature: String, publicKey: PublicKey, originalMessage: String): Boolean {
        val sig = Signature.getInstance("SHA256withRSA")
        sig.initVerify(publicKey)
        sig.update(originalMessage.toByteArray(Charsets.UTF_8))
        val signatureBytes = Base64.getDecoder().decode(signature)
        return sig.verify(signatureBytes)
    }
}