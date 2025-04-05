package com.trivaris.votechain.app.counterview

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.trivaris.votechain.voting.Candidate

@Composable
fun CandidateProgressBar(candidate: Candidate, amountAll: Int, amountThis: Int) {
    val percentage =
    if (amountAll > 0) {
        amountThis.toFloat() / amountAll
    } else 0f

    Column(modifier = Modifier.padding(8.dp)) {
        Text(text = candidate.readableName, color = Color.White)
        Row {
            LinearProgressIndicator(
                progress = percentage,
                color = Color.Green,
            )
            Text(
                text = "${"%.2f".format(percentage * 100)}% ($amountThis/$amountAll)",
                color = Color.White,
                modifier = Modifier.offset(y = (-4).dp, x = 8.dp)
            )
        }
    }
}