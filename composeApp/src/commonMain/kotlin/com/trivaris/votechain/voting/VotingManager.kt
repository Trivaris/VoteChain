package com.trivaris.votechain.voting

import androidx.compose.runtime.mutableStateOf
import com.trivaris.votechain.Cryptography
import com.trivaris.votechain.Logger
import com.trivaris.votechain.applySha256
import com.trivaris.votechain.asString
import com.trivaris.votechain.model.block.BlockRepository
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

    private fun allVotes(): MutableMap<String, String> =
        mutableMapOf<String, String>().apply {
            BlockRepository.longestChain().forEach { block ->
                block.votes.forEach { (key, value) ->
                    putIfAbsent(key, value)
                }
            }
            BlockRepository.currentVotes().forEach { (key, value) ->
                putIfAbsent(key, value)
            }
        }

    private fun countVotes(votesMap: Map<String, String> = allVotes()): MutableMap<Candidate, Int> {
        val counts = votesMap.mapNotNull { (publicKey, signature) ->
            Vote(publicKey, signature).getCandidate(decryptionMap)
        }.groupingBy { it }
            .eachCount()
            .toMutableMap()

        Candidate.entries.forEach { candidate ->
            counts.putIfAbsent(candidate, 0)
        }
        return counts
    }

    fun interpretVote(vote: Vote) {
        BlockRepository.insertVote(vote)
        Logger.PEER.log("New Vote for ${vote.getCandidate(decryptionMap)?.readableName ?: "None"}")
    }
}
