package com.trivaris.votechain.voting

import com.trivaris.votechain.Cryptography
import com.trivaris.votechain.toPublicKey
import kotlinx.serialization.Serializable

@Serializable
data class SerializableVote(
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