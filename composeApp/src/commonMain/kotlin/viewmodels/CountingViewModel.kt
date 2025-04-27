package com.trivaris.votechain.viewmodels

import androidx.compose.runtime.*
import androidx.compose.runtime.mutableStateOf
import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.trivaris.votechain.crypto.EncryptionHelper
import com.trivaris.votechain.crypto.SerializableKeypair
import com.trivaris.votechain.models.RequestState
import com.trivaris.votechain.models.candidate.CandidateRepository
import com.trivaris.votechain.models.datastore.DataStoreClient
import com.trivaris.votechain.models.datastore.PreferenceKeys
import com.trivaris.votechain.models.vote.Vote
import com.trivaris.votechain.models.vote.VoteEnvelope
import com.trivaris.votechain.models.vote.VoteRepository
import com.trivaris.votechain.models.vote.VoteResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json

typealias MutableVotes = MutableState<RequestState<List<VoteEnvelope>>>
typealias Votes = MutableState<RequestState<List<VoteEnvelope>>>

typealias MutableResults = MutableState<RequestState<List<VoteResult>>>
typealias Results = MutableState<RequestState<List<VoteResult>>>

class CountingViewModel(
    private val voteRepository: VoteRepository,
    private val candidateRepository: CandidateRepository,
) : ScreenModel {
    private val _votes: MutableVotes = mutableStateOf(RequestState.Idle)
    private val votes: Votes = _votes

    private val _results: MutableResults = mutableStateOf(RequestState.Idle)
    val results: Results = _results

    init {
        _votes.value = RequestState.Loading
        screenModelScope.launch(Dispatchers.Main) {
            voteRepository.readAllVotes().collectLatest {
                votes.value = it
            }
        }
        _results.value = RequestState.Loading
    }

    suspend fun fetchResults() {
        val decryptionKeyPref = PreferenceKeys.DECRYPTION_KEY
        val decryptionKeyJson = DataStoreClient.repo.readPref(decryptionKeyPref).last()
        val decryptionKey = Json.decodeFromString<SerializableKeypair>(decryptionKeyJson).getPrivate()

        val signingKeyPref = PreferenceKeys.SIGNING_KEY
        val signingKeyJson = DataStoreClient.repo.readPref(signingKeyPref).last()
        val signingKey = Json.decodeFromString<SerializableKeypair>(signingKeyJson).getPublic()

        val encryptionHelper = EncryptionHelper()
        val tally: MutableMap<String, VoteResult> = mutableMapOf()

        votes.value.getSuccessDataOrNull()?.forEach { envelope ->
            val voteJson = encryptionHelper.decrypt(envelope.encryptedVote, decryptionKey)
            val vote = Json.decodeFromString<Vote>(voteJson)

            val isVoteValid = encryptionHelper.verifySignature(envelope.signature, signingKey, envelope.encryptedVote)

            if (isVoteValid)
                tally.getOrPut(vote.candidateId) {
                    VoteResult(vote.candidateId, 0)
                }.count++
        }

        _results.value = RequestState.Success(tally.values.toList())
    }

    @Composable
    fun getCandidateNameById(id: String): State<String> {
        val flow = remember(id) { candidateRepository.getCandidateById(id) }
        val candidateState = flow.collectAsState(initial = RequestState.Loading)

        val fullName = when (val result = candidateState.value) {
            is RequestState.Success -> result.data?.getFullName().orEmpty()
            else -> ""
        }

        return rememberUpdatedState(fullName)
    }

}