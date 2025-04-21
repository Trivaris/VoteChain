package com.trivaris.votechain.models.candidate

import java.io.File

actual fun getCandidatesDirectory(): String {
    return File("./store/candidate/").absolutePath
}