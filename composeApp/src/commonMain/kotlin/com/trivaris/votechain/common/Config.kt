package com.trivaris.votechain.common

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import java.io.File

@Serializable
data class ConfigData(
    var difficulty: Int,
    var keypairAmount: Int,
    var serverIP: String,
    var serverPort: Int = 9235,
    var receivingPort: Int = 9235,
    var printHashCalc: Boolean,
    var debugMode: Boolean,
    var isServer: Boolean
)

object Config {
    val json = Json {
        prettyPrint = true
        encodeDefaults = true
    }
    private var file = File("config.json")
    var isAndroid = false
    var data: ConfigData =
        ConfigData(
            difficulty = 3,
            printHashCalc = true,
            keypairAmount = 10,
            serverIP = "192.168.178.70",
            debugMode = true,
            isServer = false,
        )

    fun setFile(new: File) {
        file = new
    }

    fun setSource(json: String) {
        try {
            data = Config.json.decodeFromString(json)
        } catch (e: Exception) { e.printStackTrace() }
        save()
    }

    fun save() {
        try {
            file.writeText(json.encodeToString(data))
            Logger.CONFIG.log("\n" + file.readText(), showOnAndroid = false)
        } catch (e: Exception) {
            Logger.CONFIG.log("Error saving config:", e.message.toString())
        }
    }

}