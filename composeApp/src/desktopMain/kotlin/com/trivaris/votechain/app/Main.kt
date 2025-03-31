package com.trivaris.votechain.app

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.trivaris.votechain.Config
import com.trivaris.votechain.networking.NetworkManager
import java.io.File
import java.net.InetAddress

fun main() {
    Config.setSource(getConfigJson())

    NetworkManager.join(InetAddress.getLocalHost().hostAddress)

    application {
        Window(
            onCloseRequest = ::exitApplication,
            title = "VoteChain Rewrite",
        ) {
            App({ KeyFileLoader() })
        }
    }
}

fun getConfigJson(): String {
    val file = File("config.json")
    if (!file.exists())
        file.createNewFile()
    return file.readText()
}