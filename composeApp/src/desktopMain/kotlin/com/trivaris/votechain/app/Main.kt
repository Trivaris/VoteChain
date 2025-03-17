package com.trivaris.votechain.app

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.trivaris.votechain.Config
import com.trivaris.votechain.Server
import com.trivaris.votechain.config
import com.trivaris.votechain.networking.NetworkManager
import java.io.File
import java.net.InetAddress

fun main() {
    config = Config(getConfigJson())

    NetworkManager.join(InetAddress.getLocalHost().hostAddress)

    if (config!!.data.isServer)
        Server.saveKeys()

    application {
        Window(
            onCloseRequest = ::exitApplication,
            title = "VoteChain Rewrite",
        ) {
            DesktopApp()
        }
    }
}

fun getConfigJson(): String {
    val file = File("config.json")
    if (!file.exists())
        file.createNewFile()
    return file.readText()
}