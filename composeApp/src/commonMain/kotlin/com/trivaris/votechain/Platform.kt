package com.trivaris.votechain

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform