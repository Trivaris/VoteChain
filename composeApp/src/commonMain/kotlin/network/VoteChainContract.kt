package com.trivaris.votechain.network

import com.trivaris.votechain.models.vote.Vote
import com.trivaris.votechain.models.vote.VoteEnvelope
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.hyperledger.fabric.contract.Context
import org.hyperledger.fabric.contract.ContractInterface
import org.hyperledger.fabric.contract.annotation.*
import kotlin.random.Random

@Contract(
    name = "VoteChainContract",
    info = Info(
        title = "VoteChain Smart Contract",
        description = "Secure, anonymous blockchain-based voting",
        version = "1.0.0",
        license = License(name = "Apache-2.0", url = ""),
        contact = Contact(email = "dev@votechain.org", name = "VoteChain Team", url = "https://votechain.org")
    )
)
@Default
class VoteChainContract : ContractInterface {

    @Transaction(intent = Transaction.TYPE.SUBMIT)
    fun castVote(ctx: Context, voterId: String, encryptedVote: String, signature: String) {
        val stub = ctx.stub
        if (stub.getState(voterId) != null && stub.getState(voterId).isNotEmpty()) {
            throw RuntimeException("Vote for voterId $voterId already exists")
        }
        val nonce = Random.nextInt()
        val vote = VoteEnvelope(encryptedVote, signature, nonce)
        val voteJson = Json.encodeToString(vote).toByteArray(Charsets.UTF_8)

        stub.putState(voterId, voteJson)
        stub.setEvent("VoteCast", voteJson)
    }

    @Transaction(intent = Transaction.TYPE.EVALUATE)
    fun getVote(ctx: Context, voterId: String): VoteEnvelope {
        val stub = ctx.stub
        val data = stub.getState(voterId)
            ?: throw RuntimeException("No vote found for voterId $voterId")

        return Json.decodeFromString<VoteEnvelope>(data.toString(Charsets.UTF_8))
    }

    @Transaction(intent = Transaction.TYPE.EVALUATE)
    fun getAllVotes(ctx: Context): List<VoteEnvelope> {
        val stub = ctx.stub
        val results = mutableListOf<VoteEnvelope>()

        for (kv in stub.getStateByRange("", "")) {
            val vote = Json.decodeFromString<VoteEnvelope>(kv.value.toString(Charsets.UTF_8))
            results.add(vote)
        }

        return results
    }
}