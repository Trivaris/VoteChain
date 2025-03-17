package com.trivaris.votechain.voting

import com.trivaris.votechain.Cryptography
import com.trivaris.votechain.applySha256
import com.trivaris.votechain.asString
import com.trivaris.votechain.blockchain.BlockStorage
import com.trivaris.votechain.toPublicKeyBytes
import java.security.KeyPair

object VotingManager {
    private var keypair: KeyPair? = null

    var currentCandidate: Candidate = Candidate.entries.first()
    var decryptionMap: Map<String, String> = mapOf()

    val currentVotes: MutableMap<String, String> = mutableMapOf()
    var chain = BlockStorage

    fun setKeypair(keypair: KeyPair) {
        this.keypair = keypair
    }

    fun hasVoted(): Boolean =
        allVotes().containsKey(keypair?.public?.asString())

    fun makeVote(): SerializableVote? {
        if (keypair == null) return null
        val candidateSignature = Cryptography.signData(currentCandidate.hash, keypair!!.private)
        return SerializableVote(
            keypair!!.public.asString(),
            candidateSignature
        )
    }

    private fun allVotes(): MutableMap<String, String> {
        val votes = mutableMapOf<String, String>()
        val longest = chain.longestChain()
        longest.forEach { block ->
            block.votes.forEach {
                votes.putIfAbsent(it.key, it.value)
            }
        }
        currentVotes.forEach {
            votes.putIfAbsent(it.key, it.value)
        }

        return votes
    }

    fun countVotes(votes: MutableMap<String, String> = allVotes()): MutableMap<Candidate, Int> {
        val candidateVotes: MutableMap<Candidate, Int> = mutableMapOf<Candidate, Int>()
        votes.forEach { vote ->
            val publicKey = decryptionMap[vote.key]?.toPublicKeyBytes() ?: continue
            val candidate = Candidate.entries.first { Cryptography.verifySignature(it.hash, vote.value, publicKey)}
            val newVotes = candidateVotes.getOrDefault(candidate, 0) + 1
            candidateVotes[candidate] = newVotes
        }
        return candidateVotes
    }

    fun interpretVote(vote: SerializableVote) {
        currentVotes.putIfAbsent(vote.publicKeyString, vote.candidateSignature)

        val publicKey = decryptionMap[vote.publicKeyStringHash]?.toPublicKeyBytes() ?: return
        val candidate = Candidate.entries.first {
            Cryptography.verifySignature(
                it.hash,
                vote.candidateSignature,
                publicKey
            )
        }

        println("New Vote for ${candidate.readableName}")
    }
}