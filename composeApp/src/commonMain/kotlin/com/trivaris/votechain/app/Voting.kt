package com.trivaris.votechain.app

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
import com.trivaris.votechain.networking.MessageManager
import com.trivaris.votechain.config
import com.trivaris.votechain.networking.Message
import com.trivaris.votechain.voting.VotingManager
import java.net.InetAddress

@Composable
fun Voting() {
    Row(
        modifier = Modifier.padding(8.dp)
    ) {
        CandidateSelector(
            onCandidateSelected = { candidate ->
                VotingManager.currentCandidate = candidate
            }
        )
        Spacer(modifier = Modifier.width(16.dp))

        Button(
            onClick = {
                println("Candidate: ${VotingManager.currentCandidate}")
                val vote = VotingManager.makeVote() ?: return@Button
                val voteMessage = Message(vote)
                MessageManager.outgoing(voteMessage, InetAddress.getByName(config!!.data.serverIP))
            },
            modifier = Modifier.width(100.dp).offset(y = 15.dp),
            colors = ButtonDefaults.buttonColors(backgroundColor = Color.Green),
        ) {
            Text("Vote")
        }
    }
}