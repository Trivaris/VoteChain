package com.trivaris.votechain.models.vote

import com.trivaris.votechain.models.RequestState
import com.trivaris.votechain.network.FabricGatewayClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.serialization.json.Json

class VoteRepository(
    private val gateway: FabricGatewayClient
) {
    fun castVote(
        voterId: String,
        vote: Vote
    ): Flow<RequestState<Unit>> = flow {
        emit(RequestState.Loading)
        try {
            gateway.submitTransaction(
                "CreateVote",
                voterId,
                vote.encryptedVote,
                vote.signature
            )
            emit(RequestState.Success(Unit))
        } catch (e: Exception) {
            emit(RequestState.Error(e.message ?: ""))
        }
    }.flowOn(Dispatchers.IO)

    fun readResults(): Flow<RequestState<List<VoteResult>>> = flow {
        emit(RequestState.Loading)
        try {
            val json = gateway.evaluateTransaction("GetResults")
            val results = Json.decodeFromString<List<VoteResult>>(json)
            emit(RequestState.Success(results))
        } catch (e: Exception) {
            emit(RequestState.Error(e.message ?: ""))
        }
    }.flowOn(Dispatchers.IO)
}
