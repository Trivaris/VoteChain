package com.trivaris.votechain.peer.networking

import com.trivaris.votechain.Vote
import com.trivaris.votechain.peer.Data
import io.ktor.server.application.*
import io.ktor.server.request.receive
import io.ktor.server.request.receiveParameters
import io.ktor.server.response.*
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.routing
import java.io.File

fun Application.configureRouting() {

    routing {
        // Input
        get("/")                { return@get  call.respondFile(File("resources/static/main.html")) }
        get("/debug")           { return@get  call.respondFile(File("resources/static/debug.html")) }

        // Syncing
        get("/add")             { return@get RequestHandler.add(call, call.receive<Vote>()) }
        get("/submit")          { return@get  RequestHandler.add(call, Vote(Data.privateKey, call.receiveParameters()["candidates"] as String)) }
        get("/debugSubmit")     { return@get  call.receiveParameters().let { RequestHandler.add(call, Vote(it["key"] ?: "", it["candidates"] ?: ""), it.contains("notify")) } }

        // Blocks
        post("blockMined")      { return@post RequestHandler.blockMined(call) }

        // Visualization
        get("evaluate")         { return@get  RequestHandler.evaluate(call) }
    }
}