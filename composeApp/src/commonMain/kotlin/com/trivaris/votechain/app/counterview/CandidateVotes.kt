package com.trivaris.votechain.app.counterview

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.trivaris.votechain.voting.VotingManager

@Composable
fun CandidateVotes() {
    val votes = remember { VotingManager.votes }
    val totalVotes = votes.value.values.sum()

    Column {
        votes.value.forEach { (candidate, voteCount) ->
            CandidateProgressBar(candidate, totalVotes, voteCount)
        }
    }
}