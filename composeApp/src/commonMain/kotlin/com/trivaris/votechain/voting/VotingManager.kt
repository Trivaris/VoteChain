package com.trivaris.votechain.voting

import com.trivaris.votechain.Cryptography
import com.trivaris.votechain.applySha256
import com.trivaris.votechain.asString
import com.trivaris.votechain.blockchain.BlockStorage
import com.trivaris.votechain.toPublicKeyBytes
import java.security.KeyPair

object VotingManager {
    private var keypair: KeyPair? = null

    private var currentCandidate: Candidate = Candidate.entries.first()
    private var decryptionMap: Map<String, String> = mapOf()

    val currentVotes: MutableMap<String, String> = mutableMapOf()
    private var chain = BlockStorage

    fun setKeypair(keypair: KeyPair) {
        this.keypair = keypair
    }
    fun setDecryptionMap(decryptionMap: Map<String, String>) {
        this.decryptionMap = decryptionMap
    }
    fun setCurrentCandidate(candidate: Candidate) {
        this.currentCandidate = candidate
    }

    fun hasVoted(): Boolean =
        allVotes().containsKey(keypair?.public?.asString())

    fun makeVote(): SerializableVote? {
        if (keypair == null) return null
        val candidateSignature = Cryptography.signData(currentCandidate.hash, keypair!!.private)
        return SerializableVote(
            keypair!!.public.asString().applySha256(),
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
        val candidateVotes: MutableMap<Candidate, Int> = Candidate.entries.associateWith { 0 }.toMutableMap()
        votes.forEach { vote ->
            val candidate = SerializableVote(vote.key, vote.value).getCandidate(decryptionMap) ?: return@forEach
            val newVotes = candidateVotes.getOrDefault(candidate, 0) + 1
            candidateVotes[candidate] = newVotes
        }
        return candidateVotes
    }

    fun interpretVote(vote: SerializableVote) {
        currentVotes.putIfAbsent(vote.publicKeyStringHash, vote.candidateSignature)
        println("New Vote for ${vote.getCandidate(decryptionMap)?.readableName ?: "None"}")
    }
}