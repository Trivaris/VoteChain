package com.trivaris.votechain

import io.ktor.server.application.*
import io.ktor.server.netty.EngineMain

fun main(args: Array<String>) {
    EngineMain.main(args)
}

fun Application.module() {
    moduleServer()
}

fun Application.moduleServer() {
    configureSerialization()
    configureSockets()
}
