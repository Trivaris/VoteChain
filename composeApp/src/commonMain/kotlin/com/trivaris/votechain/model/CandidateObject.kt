package com.trivaris.votechain.model

import com.trivaris.votechain.Config
import com.trivaris.votechain.applySha256
import kotlinx.serialization.Serializable

@Serializable
data class CandidateObject(
    val firstname: String,
    val lastname: String,
    val bio: String,
    val profession: String,
    val birthdate: Long
) {
    val hash: String
        get() = Config.json.encodeToString(this).applySha256()
}