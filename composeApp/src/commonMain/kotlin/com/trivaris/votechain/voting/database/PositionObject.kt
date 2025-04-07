package com.trivaris.votechain.voting.database

import com.trivaris.votechain.Config
import com.trivaris.votechain.applySha256
import kotlinx.serialization.Serializable

@Serializable
data class PositionObject(
    val name: String,
    val summary: String,
    val candidates: List<CandidateObject>
) {
    val hash: String
        get() = Config.json.encodeToString(this).applySha256()
}