package com.trivaris.votechain.app

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.trivaris.votechain.Config
import com.trivaris.votechain.app.votingview.KeyFileLoader
import com.trivaris.votechain.blockchain.BlockDatabaseManager
import com.trivaris.votechain.blockchain.database.DriverFactory
import com.trivaris.votechain.networking.NetworkManager
import com.trivaris.votechain.resources.Res
import com.trivaris.votechain.resources.icon_round
import org.jetbrains.compose.resources.painterResource
import java.io.File
import java.net.InetAddress

fun main() {
    val configFile = File("config.json")
    Config.setFile(configFile)
    Config.setSource(getConfigJson())

    val driverFactory = DriverFactory()
    BlockDatabaseManager.initDatabase(driverFactory)

    NetworkManager.join(InetAddress.getLocalHost().hostAddress)

    application {
        Window(
            onCloseRequest = ::exitApplication,
            title = "VoteChain Rewrite",
            icon = painterResource(Res.drawable.icon_round)
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