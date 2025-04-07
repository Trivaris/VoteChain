package com.trivaris.votechain.voting

import androidx.compose.runtime.mutableStateOf
import com.trivaris.votechain.Cryptography
import com.trivaris.votechain.Logger
import com.trivaris.votechain.applySha256
import com.trivaris.votechain.asString
import com.trivaris.votechain.store.block.BlockRepository
import java.security.KeyPair

object VotingManager {
    private var keypair: KeyPair? = null
    private var currentCandidate: Candidate = Candidate.entries.first()
    private var decryptionMap: Map<String, String> = emptyMap()
    private var currentVotes: MutableMap<String, String> = mutableMapOf()

    val votes = mutableStateOf(countVotes())

    fun setKeypair(newKeypair: KeyPair) {
        keypair = newKeypair
    }

    fun setDecryptionMap(newDecryptionMap: Map<String, String>) {
        decryptionMap = newDecryptionMap
    }

    val isDecryptionMapEmpty: Boolean
        get() = decryptionMap.isEmpty()

    fun updateVotes() {
        votes.value = countVotes()
    }

    fun setCurrentCandidate(candidate: Candidate) {
        currentCandidate = candidate
    }

    fun setCurrentVotes(newVotes: MutableMap<String, String>) {
        currentVotes = newVotes
    }
    fun getCurrentVotes(): MutableMap<String, String> = currentVotes.toMutableMap()
    fun removeFromCurrentVotes(toRemove: MutableMap<String, String>) = currentVotes.keys.removeAll(toRemove.keys)
    fun clearCurrentVotes() {
        Logger.DEBUG.log("Clearing Current Votes")
        currentVotes.clear()
    }

    fun makeVote(): SerializableVote? {
        return keypair?.let { kp ->
            val candidateSignature = Cryptography.signData(currentCandidate.hash, kp.private)
            SerializableVote(kp.public.asString().applySha256(), candidateSignature)
        }
    }

    private fun allVotes(): MutableMap<String, String> =
        mutableMapOf<String, String>().apply {
            BlockRepository.longestChain().forEach { block ->
                block.votes.forEach { (key, value) ->
                    putIfAbsent(key, value)
                }
            }
            currentVotes.forEach { (key, value) ->
                putIfAbsent(key, value)
            }
        }

    private fun countVotes(votesMap: Map<String, String> = allVotes()): MutableMap<Candidate, Int> {
        val counts = votesMap.mapNotNull { (publicKey, signature) ->
            SerializableVote(publicKey, signature).getCandidate(decryptionMap)
        }.groupingBy { it }
            .eachCount()
            .toMutableMap()

        Candidate.entries.forEach { candidate ->
            counts.putIfAbsent(candidate, 0)
        }
        return counts
    }

    fun interpretVote(vote: SerializableVote) {
        currentVotes.putIfAbsent(vote.publicKeyStringHash, vote.candidateSignature)
        Logger.PEER.log("New Vote for ${vote.getCandidate(decryptionMap)?.readableName ?: "None"}")
    }
}
