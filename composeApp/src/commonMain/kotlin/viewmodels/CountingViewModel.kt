package com.trivaris.votechain.viewmodels

import androidx.compose.runtime.*
import androidx.compose.runtime.mutableStateOf
import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.trivaris.votechain.models.RequestState
import com.trivaris.votechain.models.vote.VoteRepository
import com.trivaris.votechain.models.vote.VoteResult
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class CountingViewModel(
    private val voteRepository: VoteRepository
) : ScreenModel {

    private var _results = mutableStateOf<RequestState<List<VoteResult>>>(RequestState.Idle)
    val results: State<RequestState<List<VoteResult>>> = _results

    init {
        refresh()
    }

    fun refresh() {
        _results.value = RequestState.Loading
        screenModelScope.launch {
            voteRepository.readResults().collectLatest {
                _results.value = it
            }
        }
    }
}
