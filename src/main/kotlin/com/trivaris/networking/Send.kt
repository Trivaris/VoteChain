package com.trivaris.networking

import com.trivaris.blockchain.Block
import com.trivaris.blockchain.Chain
import com.trivaris.blockchain.Validity
import com.trivaris.blockchain.Validity.checkNetwork
import io.ktor.server.application.ApplicationCall
import io.ktor.server.request.receiveParameters
import io.ktor.server.response.respondRedirect
import io.ktor.server.response.respondText

object Send {
    suspend fun submit(call: ApplicationCall) {
        val requestIP = call.ip

        if (requestIP !in localHosts)
            return call.respondAccessDenied()

        val params = call.receiveParameters()
        val block = Block(data = params["candidates"] as String, uuid = Chain.userUUID())

        if (!Validity.checkLocal(block))
            return call.respondBad("Block is invalid")

        val timestamp = System.currentTimeMillis()
        if (!checkNetwork(1..Chain.peerAmount(), block, true, -1)) {
            println("[OUT] Took ${(System.currentTimeMillis() - timestamp) / 1000} seconds to perform Network Check")
            return call.respondBad("Too many bad requests")
        }
        println("[OUT] Took ${(System.currentTimeMillis() - timestamp) / 1000} seconds to perform Network Check")

        Chain.it.add(block)
        return call.respondRedirect("/")
    }

    suspend fun evaluate(call: ApplicationCall) {
        val votes = mutableMapOf<String, Int>()
        for (vote in Chain.it) {
            votes[vote.data] = votes.getOrDefault(vote.data, 0) + 1
        }
        return call.respondText(votes.toString())
    }
}