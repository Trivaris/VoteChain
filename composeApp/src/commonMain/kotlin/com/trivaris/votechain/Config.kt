package com.trivaris.votechain

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import java.io.File

@Serializable
data class ConfigData(
    val difficulty: Int,
    val printHashCalc: Boolean,
    val keypairAmount: Int,
    var serverIP: String,
    var debugMode: Boolean,
    var isServer: Boolean,
    var showLogLevels: Boolean
)

object Config {
    private val json = Json { prettyPrint = true }
    private var file = File("config.json")
    var data: ConfigData =
        ConfigData(
            difficulty = 3,
            printHashCalc = true,
            keypairAmount = 10,
            serverIP = "192.168.178.70",
            debugMode = true,
            isServer = false,
            showLogLevels = true
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
            Logger.CONFIG.log("\n" + file.readText())
        } catch (e: Exception) {
            Logger.CONFIG.log("Error saving config", e.message.toString())
        }
    }

}