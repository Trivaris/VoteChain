package com.trivaris.votechain.blockchain

import com.trivaris.votechain.Logger
import org.jetbrains.exposed.sql.Database
import java.io.File


object DatabaseManager {
    fun init(info: DatabaseInfo) {
        Logger.INFO.log("Database path: ${info.file.absolutePath}")
        Database.connect("${info.url}:${info.file.absolutePath}", driver = info.driver)
    }
}

data class DatabaseInfo(
    val file: File,
    val driver: String,
    val url: String
)