package com.trivaris.votechain

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

@Serializable
data class ConfigData(
    val difficulty: Int,
    val printHashCalc: Boolean,
    val keypairAmount: Int,
    var serverIP: String,
    var debugMode: Boolean,
    var isAndroid: Boolean,
    var isServer: Boolean
)

open class Config(sourceJson: String) {
    private val json = Json {prettyPrint = true}
    var data: ConfigData = if (sourceJson.isBlank())
        ConfigData(
            difficulty = 3,
            printHashCalc = true,
            keypairAmount = 10,
            serverIP = "192.168.178.70",
            debugMode = true,
            isAndroid = true,
            isServer = false
        )
    else
        json.decodeFromString(sourceJson)
}

var config: Config? = null