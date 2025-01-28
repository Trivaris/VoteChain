package com.trivaris.networking

import com.trivaris.blockchain.Block
import com.trivaris.blockchain.Peer
import com.trivaris.blockchain.Pair
import io.ktor.server.application.*
import io.ktor.server.request.receive
import io.ktor.server.request.receiveParameters
import io.ktor.server.response.*
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.routing
import java.io.File

val localHosts = mutableListOf<String>("127.0.0.1", "::1")

fun Application.configureRouting() {
    routing {
        get("/")                { return@get Routing.root(call)}
        get("/debug")           { return@get Routing.debug() }

        post("/submit")         { return@post RequestHandler.add(call, Pair(Peer.key, call.receiveParameters()["candidates"] as String)) }
        post("/add")            { return@post RequestHandler.add(call, call.receive<Pair>()) }

        post("/blockmined")     { return@post RequestHandler.blockMined(call) }

        get("/show")            { call.respondText { Peer.chain.toString() } }
        get("/evaluate")        { return@get RequestHandler.evaluate(call) }
    }

}

object Routing{
    suspend fun root(call: ApplicationCall) {
        if (Peer.currentVotes.getOrDefault(Peer.key, null) == null)
            call.respondFile(File("resources/static/index.html"))

        else call.respondAlreadyVoted()
    }

    suspend fun debug() {
        val newBlock = Block(votes = hashMapOf("Debug" to "None"), uuid = "127.0.0.1")
        Dispatcher.post(newBlock, localHosts[0], "add")
    }
}