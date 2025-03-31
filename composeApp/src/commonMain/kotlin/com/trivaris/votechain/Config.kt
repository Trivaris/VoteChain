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
    var isServer: Boolean
)

object Config {
    private val json = Json {prettyPrint = true}
    var data: ConfigData =
        ConfigData(
            difficulty = 3,
            printHashCalc = true,
            keypairAmount = 10,
            serverIP = "192.168.178.70",
            debugMode = true,
            isServer = false
        )

    fun setSource(json: String) {
        try {
            data = Config.json.decodeFromString(json)
        } catch (e: Exception) { e.printStackTrace() }
        save()
    }

    fun save() {
        try {
            val file = File("config.json").apply { createNewFile() }
            file.writeText(json.encodeToString(data))
            println("New Config: " + file.readText())
        } catch (e: Exception) {
            println("Error saving config: ${e.message}")
        }
    }

}