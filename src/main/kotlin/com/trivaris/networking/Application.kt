package com.trivaris.networking

import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import java.security.MessageDigest

fun main() {
    embeddedServer(Netty, port = 8080, host = "0.0.0.0", module = Application::module)
        .start(wait = true)
}

fun Application.module() {
    configureHTTP()
    configureRouting()
}

fun String.applySha256(): String {
    val digest = MessageDigest.getInstance("SHA-256")
    val hash = digest.digest(this.toByteArray(Charsets.UTF_8))
    return hash.joinToString("") { String.format("%02x", it) }
}