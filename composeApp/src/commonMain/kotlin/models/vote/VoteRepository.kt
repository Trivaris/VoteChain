package com.trivaris.votechain.models.vote

import com.trivaris.votechain.models.RequestState
import kotlinx.coroutines.flow.Flow

class VoteRepository {
    fun castVote(
        voterId: String,
        vote: VoteEnvelope
    ): Flow<RequestState<Boolean>> = TODO()

    fun readAllVotes(): Flow<RequestState<List<VoteEnvelope>>> = TODO()
}