package com.trivaris.votechain.view.votingview

import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.trivaris.votechain.network.NetworkManager

@Composable
fun JoinRequestButton() {
    Button(
        onClick = {
            NetworkManager.join()
        },
        colors = ButtonDefaults.buttonColors(backgroundColor = Color.Green),
    ) { Text("Join Network") }
}