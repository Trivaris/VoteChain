package com.trivaris.votechain.vote

import com.trivaris.votechain.common.applySha256

enum class Candidate {
    ANNETTE_ANNERHOFF,
    BASTIAN_BEER,
    CARSTEN_CAHN;

    val hash: String
        get() = name.applySha256()
    val readableName: String
        get() = name.lowercase().split("_").joinToString(" ") { it.replaceFirstChar { it.uppercase() } }
}