package com.trivaris.votechain.view.votingview

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.trivaris.votechain.common.Logger
import com.trivaris.votechain.network.NetworkManager
import com.trivaris.votechain.vote.VotingManager

@Composable
fun Voting(onVoteNull: () -> Unit) {
    Row(
        modifier = Modifier.padding(8.dp)
    ) {
        CandidateSelector(
            onCandidateSelected = { candidate ->
                VotingManager.setCurrentCandidate(candidate)
            }
        )
        Spacer(modifier = Modifier.width(16.dp))

        Button(
            onClick = {
                val vote = VotingManager.makeVote()
                if (vote == null) {
                    Logger.INFO.log("Vote was null")
                    onVoteNull()
                    return@Button
                }
                Logger.PEER.log("Sending Vote")
                NetworkManager.broadcast(vote)
            },
            modifier = Modifier.width(100.dp).offset(y = 15.dp),
            colors = ButtonDefaults.buttonColors(backgroundColor = Color.Green),
        ) {
            Text("Vote")
        }
    }
}