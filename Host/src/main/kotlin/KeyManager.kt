package com.trivaris.votechain.server

import com.trivaris.votechain.applySha256
import java.security.KeyPair
import java.security.KeyPairGenerator

object KeyManager {
    const val KEY_LENGTH = 2048
    const val KEY_AMOUNT = 10

    // Private to decode, so it should be the one that's made public
    val keyPairs = List(KEY_AMOUNT) { generateKeyPair() }
    val keyMap = keyPairs.associate { it.private.toString().applySha256() to it }

    fun generateKeyPair(): KeyPair {
        val keyPairGenerator = KeyPairGenerator.getInstance("RSA")
        keyPairGenerator.initialize(KEY_LENGTH)
        return keyPairGenerator.generateKeyPair()
    }

}
