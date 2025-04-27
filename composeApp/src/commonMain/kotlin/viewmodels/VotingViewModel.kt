package com.trivaris.votechain.viewmodels

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.trivaris.votechain.crypto.EncryptionHelper
import com.trivaris.votechain.crypto.SerializableKeypair
import com.trivaris.votechain.models.RequestState
import com.trivaris.votechain.models.candidate.CandidateObject
import com.trivaris.votechain.models.candidate.CandidateRepository
import com.trivaris.votechain.models.datastore.DataStoreClient
import com.trivaris.votechain.models.datastore.PreferenceKeys
import com.trivaris.votechain.models.vote.Vote
import com.trivaris.votechain.models.vote.VoteEnvelope
import com.trivaris.votechain.models.vote.VoteRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.launch
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

typealias MutableCandidates = MutableState<RequestState<List<CandidateObject>>>
typealias Candidates = State<RequestState<List<CandidateObject>>>

class VotingViewModel(
    private val candidateRepository: CandidateRepository,
    private val voteRepository: VoteRepository
) : ScreenModel {
    private var _candidates: MutableCandidates = mutableStateOf(RequestState.Idle)
    val candidates: Candidates = _candidates

    private var _selectedCandidate: MutableState<CandidateObject?> = mutableStateOf(null)
    val selectedCandidate: State<CandidateObject?> = _selectedCandidate

    init {
        _candidates.value = RequestState.Loading
        screenModelScope.launch(Dispatchers.Main) {
            delay(500)
            candidateRepository.readCandidates().collectLatest {
                _candidates.value = it
            }
        }
    }

    fun setSelectedCandidate(candidate: CandidateObject) {
        _selectedCandidate.value = candidate
    }

    fun castVote() {
        screenModelScope.launch(Dispatchers.Main) {
            val voterIdPref = PreferenceKeys.VOTER_ID
            val voterId = DataStoreClient.repo.readPref(voterIdPref).last()

            val encryptionKeyPref = PreferenceKeys.DECRYPTION_KEY
            val encryptionKeyJson = DataStoreClient.repo.readPref(encryptionKeyPref).last()
            val encryptionKey = Json.decodeFromString<SerializableKeypair>(encryptionKeyJson).getPublic()

            val vote = Vote(selectedCandidate.value?.getId() ?: throw Exception("No candidate selected"))
            val encryptedVote = EncryptionHelper().encrypt(Json.encodeToString(vote), encryptionKey)

            val voteEnvelope = VoteEnvelope(encryptedVote, "")

            voteRepository.castVote(voterId, voteEnvelope)
        }
    }

}