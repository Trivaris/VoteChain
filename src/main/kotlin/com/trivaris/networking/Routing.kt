package com.trivaris.networking

import com.trivaris.blockchain.Block
import com.trivaris.blockchain.Peer
import com.trivaris.blockchain.Vote
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
        // Input
        get("/")                { return@get Routing.root(call, "index")}
        get("/custom")          { return@get Routing.root(call, "customSubmit")}
        get("/debug")           { return@get Routing.debug() }

        // Syncing
        post("/submit")         { return@post RequestHandler.add(call, Vote(Peer.key, call.receiveParameters()["candidates"] as String)) }
        post("/add")            { return@post RequestHandler.add(call, call.receive<Vote>()) }
        post("/customSubmit")   { val params = call.receiveParameters(); return@post RequestHandler.add(call, Vote(params["key"] as String, params["candidates"] as String)) }
        post("/blockmined")     { return@post RequestHandler.blockMined(call) }

        // Show data
        get("/show")            { call.respondText { Peer.chain.toString() } }
        get("/evaluate")        { return@get RequestHandler.evaluate(call) }
        get("/current")         { call.respondText(Peer.currentVotes.toString()) }
        get("/config")          { call.respondText("""
            Children: ${Peer.children},
            Parent: ${Peer.parent},
        """.trimIndent()) }

        // Commands
        get("/cleanup")         { Peer.cleanup() }
        get("/mine")            { Peer.mineLatest() }
        get("/update")          { Peer.update() }

        get("/getChain")        { call.respond(Peer.chain)}
        get("/getCurrent")      { call.respond(Peer.currentVotes)}

    }

}

object Routing{
    suspend fun root(call: ApplicationCall, html: String) {
        if (Peer.currentVotes.getOrDefault(Peer.key, null) == null || html == "customSubmit")
            call.respondFile(File("resources/static/$html.html"))

        else call.respondAlreadyVoted()
    }

    suspend fun debug() {
        val newBlock = Block(votes = hashMapOf("Debug" to "None"))
        Dispatcher.post(newBlock, localHosts[0], "add")
    }
}