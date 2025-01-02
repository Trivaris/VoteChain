package com.trivaris.networking

import com.trivaris.blockchain.Block
import com.trivaris.blockchain.Chain
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.ApplicationCall
import io.ktor.server.response.respondRedirect

const val badRequestThreshold = 10

suspend fun submit(vote: Block, call: ApplicationCall) {
    val nodes = Chain.getVoters()
    val badRequests = nodes.count { Dispatcher.post(vote, it, "probe").status == HttpStatusCode.BadRequest }
    if (badRequests != badRequestThreshold) return call.respondBad()

    nodes.forEach { Dispatcher.post(vote, it, "add") }
    call.respondRedirect("/")
}