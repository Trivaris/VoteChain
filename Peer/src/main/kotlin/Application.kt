package com.trivaris.votechain.peer

import com.trivaris.votechain.decrypt
import com.trivaris.votechain.encrypt
import com.trivaris.votechain.peer.networking.Dispatcher
import com.trivaris.votechain.peer.networking.configureRouting
import io.ktor.http.HttpStatusCode
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.application.*
import io.ktor.server.netty.EngineMain
import io.ktor.server.plugins.defaultheaders.DefaultHeaders
import io.ktor.server.plugins.statuspages.StatusPages
import io.ktor.server.response.respondText
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import java.security.KeyPair
import java.security.KeyPairGenerator

fun main(args: Array<String>) {
    Dispatcher.main()
    //EngineMain.main(args)
}

fun Application.modulePeer() {
    setup()
    configureHTTP()
    configureSerialization()
    configureStatusPages()
    configureRouting()
}

fun Application.setup() {
    //launch { Dispatcher.connectToServer() }
}

fun Application.configureHTTP() {
    install(DefaultHeaders) {
        header("X-Engine", "Ktor")
    }
}

fun Application.configureSerialization() {
    install(ContentNegotiation) {
        json(Json {
            prettyPrint = true
            isLenient = true
        })
    }
}

fun Application.configureStatusPages() {
    install(StatusPages) {
        exception<Throwable> { call, cause ->
            call.respondText(text = "500: $cause", status = HttpStatusCode.InternalServerError)
        }
    }
}

fun generateKeyPair(): KeyPair {
    val keyPairGenerator = KeyPairGenerator.getInstance("RSA")
    keyPairGenerator.initialize(2048)
    return keyPairGenerator.generateKeyPair()
}

fun debug() {
    val keyPair = generateKeyPair()
    val clearText = "Hello World"
    val encrypted = encrypt(keyPair.public, clearText)
    println("Encrypt: ${encrypted}")
    println("Decrypt: ${decrypt(keyPair.private, encrypted)}")
}