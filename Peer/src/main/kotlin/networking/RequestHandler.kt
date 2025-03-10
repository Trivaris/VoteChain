package com.trivaris.votechain.peer.networking

import com.trivaris.votechain.applySha256
import com.trivaris.votechain.ip
import com.trivaris.votechain.blockchain.Block
import com.trivaris.votechain.Vote
import com.trivaris.votechain.peer.Data
import com.trivaris.votechain.isSupersetOf
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.ApplicationCall
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.response.respondText

object RequestHandler {

    suspend fun add(call: ApplicationCall, vote: Vote, notify: Boolean = true) {
        if (call.ip !in Data.recipients) return call.respondAccessDenied()

        if (vote.publicKeyHash in Data.allVotes.map { it.publicKeyHash })
            return call.respondBad("That key already voted!")

        Data.currentVotes += vote

        //if (notify) Dispatcher.post(vote, Data.recipients, "add")

        return call.respond(HttpStatusCode.OK)
    }

    suspend fun blockMined(call: ApplicationCall) {
        if (call.ip !in Data.recipients) return call.respondAccessDenied()

        val block = call.receive<Block>()
        val validity = block.validity()
        if (validity != null) return call.respondBad(validity)
        if (!block.votes.isSupersetOf(Data.currentVotes)) return call.respondBad("Votes don't match")

        Data.currentVotes.clear()
        Data.chain += block
    }

    suspend fun evaluate(call: ApplicationCall) {
        if (call.ip !in Data.localHosts) return call.respondAccessDenied()

        return call.respondText("""
            Votes: ${Data.allVotes}
            Filtered: ${Data.allVotes.filter { it.publicKeyHash !in Data.publicKeys.map { it.applySha256() } }}
        """.trimIndent())
    }

}

suspend fun ApplicationCall.respondBad(message: String) =
    respond(HttpStatusCode.BadRequest, message)

suspend fun ApplicationCall.respondAccessDenied() =
    respond(HttpStatusCode.Forbidden, "Access denied")