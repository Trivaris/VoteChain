package com.trivaris.blockchain

import com.trivaris.networking.Dispatcher
import com.trivaris.networking.ip
import io.ktor.client.call.body
import io.ktor.client.statement.HttpResponse
import io.ktor.http.isSuccess
import io.ktor.server.application.ApplicationCall
import kotlinx.coroutines.runBlocking
import org.json.JSONObject
import java.io.File
import kotlin.String
import kotlin.collections.hashMapOf
import kotlin.io.path.fileVisitor

object Peer {
    var parent = ""
    var children = mutableListOf<String>()
    var ip = ""

    var key= ""
    var allowedKeys = mutableListOf<String>()

    var chain = mutableListOf<Block>()
    var currentVotes = hashMapOf<String, String>()


    fun initialize() {
        val file = File("VCconfig.json")

        if (!file.exists()) {
            val defaultConfig = JSONObject().apply {
                put("parent", parent)
                put("children", children)
                put("key", key)
                put("ip", ip)
                put("allowedKeys", allowedKeys)
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
        val allowedKeysArray = jsonObject.getJSONArray("allowedKeys")

        for (i in 0 until childrenArray.length())
            children.add(childrenArray.getString(i))

        for (i in 0 until allowedKeysArray.length())
            allowedKeys.add(allowedKeysArray.getString(i))
    }

    fun print() {
        println("Parent: $parent")
        println("Key: $key")
        println("Children: $children")
    }

    fun cleanup() {
        chain.clear()
        currentVotes.clear()
    }

    suspend fun update() {
        val responseChain = Dispatcher.get(parent, "getChain")
        val responseCurrentVotes = Dispatcher.get(parent, "getCurrent")

        if (!responseChain.status.isSuccess() || !responseCurrentVotes.status.isSuccess()) return

        chain = responseChain.body<MutableList<Block>>()
        currentVotes = responseCurrentVotes.body<HashMap<String, String>>()
    }

    suspend fun mineLatest() {
        val block = chain.lastOrNull() ?: Block()

        block.mine()

        Dispatcher.post(block, getRecipients(), "blockmined")
    }

    fun allVotes(): MutableMap<String, String> {
        val votes = mutableMapOf<String, String>()
        currentVotes.forEach { key, value -> votes.putIfAbsent(key, value) }
        chain.forEach { block ->
            block.votes.forEach { key, value -> votes.putIfAbsent(key, value) }
        }
        println(votes.toString())
        return votes
    }

    fun getRecipients(callIP: String = ""): MutableList<String> {
        val recipients = mutableListOf<String>()
        recipients.addAll(children)
        recipients.add(parent)
        recipients.remove(callIP)
        recipients.removeAll { it == "" }

        return recipients
    }
    fun getRecipients(call: ApplicationCall): MutableList<String> {
        return getRecipients(call.ip)
    }
}