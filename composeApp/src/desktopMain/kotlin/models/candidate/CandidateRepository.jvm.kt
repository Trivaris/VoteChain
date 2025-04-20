package com.trivaris.votechain.models.candidate

import java.io.File

actual fun getRealmDirectory(): String {
    return File("./store/").absolutePath
}