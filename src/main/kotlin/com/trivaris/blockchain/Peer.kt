package com.trivaris.blockchain

import org.json.JSONObject
import java.io.File

object Peer {
    var parent: String = ""
    var children: MutableList<String> = mutableListOf()
    var ip: String = ""

    var key: String = ""

    val chain = mutableListOf<Block>()
    val currentVotes = hashMapOf<String, String>()


    fun initialize() {
        val file = File("VCconfig.json")

        if (!file.exists()) {
            val defaultConfig = JSONObject().apply {
                put("parent", parent)
                put("children", children)
                put("key", key)
                put("ip", ip)
            }

            file.writeText(defaultConfig.toString())
        }

        val jsonContent = file.readText()
        val jsonObject = JSONObject(jsonContent)

        key = jsonObject.getString("key")

        ip = jsonObject.getString("ip")
        parent = jsonObject.getString("parent")
        children = mutableListOf<String>()
        val childrenArray = jsonObject.getJSONArray("children")

        for (i in 0 until childrenArray.length())
            children.add(childrenArray.getString(i))
    }

    fun print() {
        println("Parent: $parent")
        println("Key: $key")
        println("Children: $children")
    }
}