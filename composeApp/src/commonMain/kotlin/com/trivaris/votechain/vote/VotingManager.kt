package com.trivaris.votechain.vote

import androidx.compose.runtime.mutableStateOf
import com.trivaris.votechain.common.Cryptography
import com.trivaris.votechain.common.Logger
import com.trivaris.votechain.common.applySha256
import com.trivaris.votechain.common.asString
import com.trivaris.votechain.model.block.ChainInteraction
import java.security.KeyPair

object VotingManager {
    private var keypair: KeyPair? = null
    private var currentCandidate: Candidate = Candidate.entries.first()
    private var decryptionMap: Map<String, String> = emptyMap()

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

    fun makeVote(): Vote? {
        return keypair?.let { kp ->
            val candidateSignature = Cryptography.signData(currentCandidate.hash, kp.private)
            Vote(kp.public.asString().applySha256(), candidateSignature)
        }
    }

    private fun allVotes(): List<Vote> {
        val blocks = ChainInteraction.longestChain()
        val votes = blocks.map { it.votes }.flatten().toMutableList()
        Logger.INFO.log("Longest Chain: ${votes.size}")
        return votes
    }

    private fun countVotes(votesList: List<Vote> = allVotes()): MutableMap<Candidate, Int> {
        val counts = votesList.mapNotNull {
            it.getCandidate(decryptionMap)
        }.groupingBy { it }
            .eachCount()
            .toMutableMap()

        Candidate.entries.forEach { candidate ->
            counts.putIfAbsent(candidate, 0)
        }
        return counts
    }

    fun interpretVote(vote: Vote) {
        ChainInteraction.insertVote(vote)
        Logger.PEER.log("New Vote for ${vote.getCandidate(decryptionMap)?.readableName ?: "None"}")
    }
}
