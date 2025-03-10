package com.trivaris.votechain.server

import io.ktor.server.application.Application
import io.ktor.server.routing.get
import io.ktor.server.routing.routing
import io.ktor.util.reflect.TypeInfo
import io.ktor.util.reflect.typeInfo
import java.security.PrivateKey

val keyMapInfo: TypeInfo = typeInfo<MutableMap<String, PrivateKey>>()

fun Application.configureRouting() {
    routing {
        get("/getKeys")   { return@get call.respond(KeyManager.keyMap, keyMapInfo) }
    }
}