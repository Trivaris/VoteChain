package com.trivaris.votechain.view.counterview

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.trivaris.votechain.vote.VotingManager

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