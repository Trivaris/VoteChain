package com.trivaris.votechain.blockchain

import org.jetbrains.exposed.sql.Table


object BlocksTable : Table("blocks") {
    val hash = varchar("hash", 64)
    val votes = text("votes")
    val previousHash = varchar("previous_hash", 64)
    val timestamp = long("timestamp")
    val nonce = integer("nonce")

    override val primaryKey = PrimaryKey(hash, name = "PK_Blocks")
}