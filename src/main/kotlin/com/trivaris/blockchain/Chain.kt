package com.trivaris.blockchain

import com.trivaris.applySha256
import java.io.File

object Chain {

    val it = mutableListOf<Block>()
    private val uuidFile = File("resources/uuid.txt")

    fun userIP() = uuidFile.readLines()[0].toInt()
    fun userUUID() = userIP().toString().applySha256()

    fun peerAmount() = uuidFile.readLines()[1].toInt()
    fun voterUUIDs() =  it.map { it.uuid } as ArrayList<String>
    fun userVoted() = Validity.alreadyVoted(userUUID())

}