package com.trivaris.votechain.models.vote

import com.trivaris.votechain.models.RequestState
import com.trivaris.votechain.network.FabricGatewayClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onStart
import kotlinx.serialization.json.Json

class VoteRepository(
    private val gateway: FabricGatewayClient
) {
    fun castVote(
        voterId: String,
        vote: VoteEnvelope
    ): Flow<RequestState<Unit>> = flow {
        emit(RequestState.Loading)
        try {
            gateway.submitTransaction(
                "castVote",
                voterId,
                vote.encryptedVote,
                vote.signature
            )
            emit(RequestState.Success(Unit))
        } catch (e: Exception) {
            emit(RequestState.Error(e.message ?: "Unknown error"))
        }
    }.flowOn(Dispatchers.IO)

    fun readAllVotes(): Flow<RequestState<List<VoteEnvelope>>> = callbackFlow {
        try {
            val json = gateway.evaluateTransaction("getAllVotes")
            val votes = Json.decodeFromString<List<VoteEnvelope>>(json)
            trySend(RequestState.Success(votes))
        } catch (e: Exception) {
            trySend(RequestState.Error(e.message ?: "Initial load failed"))
        }

        val listener = gateway.contract.addContractListener { event ->
            try {
                val json2 = event.payload.toString()
                val votes2 = Json.decodeFromString<List<VoteEnvelope>>(json2)
                trySend(RequestState.Success(votes2))
            } catch (e: Exception) {
                trySend(RequestState.Error(e.message ?: "Update failed"))
            }
        }

        awaitClose {
            gateway.contract.removeContractListener(listener)
        }
    }
        .onStart { emit(RequestState.Loading) }
        .flowOn(Dispatchers.IO)
}