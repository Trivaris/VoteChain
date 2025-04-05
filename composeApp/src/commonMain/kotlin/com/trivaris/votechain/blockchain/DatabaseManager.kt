package com.trivaris.votechain.blockchain

import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction

object DatabaseManager {
    fun init() {
        Database.connect("jdbc:sqlite:blocks.db", driver = "org.sqlite.JDBC")
        transaction {
            SchemaUtils.create(BlocksTable)
        }
    }
}