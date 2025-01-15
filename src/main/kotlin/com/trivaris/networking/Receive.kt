package com.trivaris.networking

import com.trivaris.blockchain.Block
import com.trivaris.blockchain.Chain
import com.trivaris.blockchain.Validity
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.ApplicationCall
import io.ktor.server.plugins.origin
import io.ktor.server.request.receive
import io.ktor.server.response.respond

object Receive {
    val probes = hashMapOf<Block, Int>()

    suspend fun probe(call: ApplicationCall) {
        val block = call.receive<Block>()

        if (Validity.checkLocal(block))
            return call.respondBad("Invalid Block")

//        if (!Validity.checkNetwork(1..<Chain.userIP(), block, false, call.ip.substringAfterLast(".").toInt()))
//            return call.respondBad("Too many bad Requests")

        probes[block] = probes.getOrDefault(block, 0) + 1

        return call.respond(HttpStatusCode.OK)
    }

    suspend fun add(call: ApplicationCall) {
        val block = call.receive<Block>()

        if (!Validity.checkLocal(block))
            return call.respondBad("Invalid Block")

        if(!Validity.checkNetwork((Chain.userIP() + 1)..Chain.peerAmount(), block, false, call.ip.substringAfterLast(".").toInt()))
            return call.respondBad("Too many bad Requests")

        Chain.it.add(block)
        return call.respond(HttpStatusCode.OK)
    }

}

suspend fun ApplicationCall.respondAlreadyVoted() =
    respond(HttpStatusCode.Forbidden, "You already voted")

suspend fun ApplicationCall.respondAccessDenied() =
    respond(HttpStatusCode.Forbidden, "Access denied")

suspend fun ApplicationCall.respondBad(msg: String) =
    respond(HttpStatusCode.BadRequest, msg)

val ApplicationCall.ip: String
    get() = this.request.origin.remoteAddress