package com.trivaris.votechain.peer

import com.trivaris.votechain.Vote
import com.trivaris.votechain.blockchain.Chain
import kotlin.String

object Data {
    var privateKey = ""
    var publicKey = ""
    val publicKeys = mutableListOf<String>()

    val chain = Chain()
    val currentVotes = mutableListOf<Vote>()
    val allVotes: MutableList<Vote>
        get() = (chain.votes + currentVotes) as MutableList<Vote>

    var server = ""
    val recipients = mutableListOf<String>()
    val localHosts = mutableListOf<String>("127.0.0.1", "::1")
}