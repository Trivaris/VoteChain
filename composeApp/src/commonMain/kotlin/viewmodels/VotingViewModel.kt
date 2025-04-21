package com.trivaris.votechain.viewmodels

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.trivaris.votechain.models.RequestState
import com.trivaris.votechain.models.candidate.CandidateObject
import com.trivaris.votechain.models.candidate.CandidateRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

typealias MutableCandidates = MutableState<RequestState<List<CandidateObject>>>
typealias Candidates = MutableState<RequestState<List<CandidateObject>>>

class VotingViewModel(private val candidateRepository: CandidateRepository) : ScreenModel {
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

}