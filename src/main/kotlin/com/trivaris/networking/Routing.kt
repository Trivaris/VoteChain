package com.trivaris.networking

import com.trivaris.blockchain.Block
import com.trivaris.blockchain.Chain
import io.ktor.server.application.*
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

        post("/submit")         { return@post Send.submit(call) }
        post("/add")            { return@post Receive.add(call) }
        post("/probe")          { return@post Receive.probe(call) }

        get("/show")            { call.respondText { Chain.it.toString() } }
        get("/evaluate")        { return@get Send.evaluate(call) }
    }

}

object Routing{
    suspend fun root(call: ApplicationCall) {
        if (!Chain.userVoted()) call.respondFile(File("resources/static/index.html"))
        else call.respondAlreadyVoted()
    }

    suspend fun debug() {
        val newBlock = Block(data = "DebugBlock", uuid = "127.0.0.1")
        Dispatcher.post(newBlock, localHosts[0], "add")
    }
}