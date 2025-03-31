package com.trivaris.votechain.app.counterview

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.trivaris.votechain.voting.Candidate
import com.trivaris.votechain.voting.VotingManager

@Composable
fun CandidateVotes(votes: MutableMap<Candidate, Int>) {
    val totalVotes = votes.values.sum()

    Column {
        votes.forEach { (candidate, voteCount) ->
            val percentage = if (totalVotes > 0) {
                voteCount.toFloat() / totalVotes
            } else 0f

            CandidateProgressBar(candidate, percentage)
        }
    }
}

@Composable
fun CandidateProgressBar(candidate: Candidate, percentage: Float) {
    Column(modifier = Modifier.padding(8.dp)) {
        Text(text = candidate.readableName, color = Color.White)
        Row {
            LinearProgressIndicator(
                progress = percentage,
                color = Color.Green,
            )
            Text(
                text = "${"%.2f".format(percentage * 100)}%",
                color = Color.White,
                modifier = Modifier.offset(y = (-4).dp, x = 8.dp)
            )
        }
    }
}

@Composable
fun CountVotesButton(allVotes: MutableState<MutableMap<Candidate, Int>>) {
    Button(
        onClick = {
            val updatedVotes = VotingManager.countVotes()
            println("Counted Votes = $updatedVotes")
            allVotes.value = updatedVotes
        },
        colors = ButtonDefaults.buttonColors(backgroundColor = Color.Green),
    ) {
        Text("Count Votes")
    }
}
