package com.trivaris.networking

import com.trivaris.blockchain.Block
import com.trivaris.blockchain.Pair
import com.trivaris.blockchain.Peer
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.ApplicationCall
import io.ktor.server.plugins.origin
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.response.respondText

object RequestHandler {

    suspend fun add(call: ApplicationCall, vote: Pair) {
        if (call.ip !in Peer.children && call.ip != Peer.parent && call.ip != Peer.ip) return call.respondAccessDenied()

        println("[OUT] User eligble")

        val recipients = Peer.children
        recipients.add(Peer.parent)
        recipients.remove(call.ip)

        recipients.removeAll { it == "" }
        println("[OUT] Recipients: $recipients")

        Peer.currentVotes[vote.first] = vote.second
        Dispatcher.post(vote, recipients, "add")

        return call.respond(HttpStatusCode.OK)
    }

    suspend fun blockMined(call: ApplicationCall) {
        if (call.ip !in Peer.children || call.ip != Peer.parent) return call.respondAccessDenied()

        val block = call.receive<Block>()
        val validity = block.validity()
        if (validity != null) return call.respondBad(validity)

        val recipients = Peer.children
        recipients.add(Peer.parent)
        recipients.remove(call.ip)

        Peer.currentVotes.clear()
        Dispatcher.post(block, recipients, "blockmined")
    }

    suspend fun evaluate(call: ApplicationCall) {
        val votes = mutableMapOf<String, Int>()
        for (block in Peer.chain) {
            val blockVotes = block.votes
            for (candidate in blockVotes.values)
                votes[candidate] = votes.getOrDefault(candidate, 0) + 1
        }

        for (candidate in Peer.currentVotes.values)
            votes[candidate] = votes.getOrDefault(candidate, 0) + 1

        return call.respondText(votes.toString())
    }

}

suspend fun ApplicationCall.respondAlreadyVoted() =
    respond(HttpStatusCode.Forbidden, "You already voted")

suspend fun ApplicationCall.respondBad(message: String) =
    respond(HttpStatusCode.BadRequest, message)

suspend fun ApplicationCall.respondAccessDenied() =
    respond(HttpStatusCode.Forbidden, "Access denied")

val ApplicationCall.ip: String
    get() = this.request.origin.remoteAddress