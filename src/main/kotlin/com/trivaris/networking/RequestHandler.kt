package com.trivaris.networking

import com.trivaris.applySha256
import com.trivaris.blockchain.Block
import com.trivaris.blockchain.Vote
import com.trivaris.blockchain.Peer
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.ApplicationCall
import io.ktor.server.plugins.origin
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.response.respondText

object RequestHandler {

    suspend fun add(call: ApplicationCall, vote: Vote, notify: Boolean = true) {
        if (
            call.ip !in Peer.children &&
            call.ip != Peer.parent &&
            call.ip != Peer.ip &&
            call.ip !in localHosts
            )

            return call.respondAccessDenied()

        println("[OUT] User eligble")

        val recipients = Peer.getRecipients(call)

        println("[OUT] Recipients: $recipients")

        if (vote.key in Peer.allVotes()) return call.respondBad("That key already voted!")

        Peer.currentVotes[vote.key] = vote.vote

        if (notify) Dispatcher.post(vote, recipients, "add")

        return call.respond(HttpStatusCode.OK)
    }

    suspend fun blockMined(call: ApplicationCall) {
        if (call.ip !in Peer.children && call.ip != Peer.parent) return call.respondAccessDenied()

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
        val votes = Peer.allVotes().values.groupingBy { it }.eachCount()
        val filteredVotes = Peer.allVotes().filterKeys { it.applySha256() in Peer.allowedKeys }.values.groupingBy { it }.eachCount()

        return call.respondText("""
            Votes: $votes
            Filtered: $filteredVotes
        """.trimIndent())
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