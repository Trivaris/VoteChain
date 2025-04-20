package com.trivaris.votechain.models.candidate

import kotlinx.cinterop.ExperimentalForeignApi
import platform.Foundation.NSFileManager
import platform.Foundation.NSDocumentDirectory

@OptIn(ExperimentalForeignApi::class)
actual fun getRealmDirectory(): String {
    val fileManager = NSFileManager.defaultManager
    val documentDirectory = fileManager.URLForDirectory(
        NSDocumentDirectory,
        inDomain = 1u,
        appropriateForURL = null,
        create = true,
        error = null
    )
    val rootpath = documentDirectory?.path ?: throw Exception("Failed to get document directory path.")
    return "$rootpath/store/"
}