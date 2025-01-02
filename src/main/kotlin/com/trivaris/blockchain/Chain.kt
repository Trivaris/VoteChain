package com.trivaris.blockchain

import com.trivaris.networking.applySha256
import java.io.File

object Chain {

    val it = arrayListOf(Block(data = "data", hashedIP = "0"))
    val ownIP = File("resources/ip.txt").readLines()[0].applySha256()

    fun getVoters(): ArrayList<String> =
        it.map { it.hashedIP } as ArrayList<String>

    fun add(block: Block) =
        Validity.checkAsNewest(block).takeIf { it }?.let { this.it.add(block) }

    fun selfVoted() =
        getVoters().contains(ownIP)
}