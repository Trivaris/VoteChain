package com.trivaris.networking

import com.trivaris.blockchain.Block
import com.trivaris.blockchain.Chain
import com.trivaris.blockchain.Validity
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.plugins.origin
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.request.receive
import io.ktor.server.request.receiveParameters
import io.ktor.server.response.*
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.routing
import java.io.File

val localHosts = mapOf("v4" to "127.0.0.1","v6" to "::1")

fun Application.configureRouting() {
    install(StatusPages) {
        exception<Throwable> { call, cause ->
            call.respondText(text = "500: $cause", status = HttpStatusCode.InternalServerError)
        }
    }

    routing {

        // UI
        get("/") {
            if (!Chain.selfVoted()) call.respondFile(File("resources/static/index.html"))
            else call.respondAlreadyVoted()
        }

        // Vote
        post("/submit") {
            val requestIP = call.request.origin.remoteHost

            if (Chain.selfVoted())               return@post call.respondAlreadyVoted()
            if (requestIP !in localHosts.values) return@post call.respondAccessDenied()

            val params = call.receiveParameters()
            val block = Block(data = params["candidates"] as String, hashedIP = Chain.ownIP)

            submit(block, call)
        }

        // Receive Vote
        post("/add") {
            val block = call.receive<Block>()
            if (!Validity.checkAsNewest(block)) return@post call.respondBad()
            else Chain.add(block)
        }

        post("/probe") {
            val block = call.receive<Block>()
            val isGood = Validity.checkAsNewest(block)
            call.respond(if (isGood) HttpStatusCode.OK else HttpStatusCode.BadRequest)
        }

        get("/debug") {
            val newBlock = Block(data = "DebugBlock", hashedIP = "127.0.0.1")
            Dispatcher.post(newBlock, localHosts["v4"]!!, "add")
        }
    }

}

suspend fun ApplicationCall.respondAlreadyVoted() =
    respond(HttpStatusCode.Forbidden, "You already voted")
suspend fun ApplicationCall.respondAccessDenied() =
    respond(HttpStatusCode.Forbidden, "Acccess denied")
suspend fun ApplicationCall.respondBad() =
    respond(HttpStatusCode.BadRequest)