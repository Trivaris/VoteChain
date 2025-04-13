package com.trivaris.votechain.vote

import com.trivaris.votechain.common.Cryptography
import com.trivaris.votechain.common.toPublicKey
import kotlinx.serialization.Serializable

@Serializable
data class Vote(
    val publicKeyStringHash: String,
    val candidateSignature: String
) {
    fun getCandidate(decryptionMap: Map<String, String>): Candidate? {
        val publicKey = decryptionMap[this.publicKeyStringHash]?.toPublicKey() ?: return null
        return Candidate.entries.first {
            Cryptography.verifySignature(
                it.hash,
                this.candidateSignature,
                publicKey
            )
        }
    }
}