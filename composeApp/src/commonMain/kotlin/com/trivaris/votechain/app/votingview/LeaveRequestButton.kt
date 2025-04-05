package com.trivaris.votechain.app.votingview

import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.trivaris.votechain.networking.NetworkManager

@Composable
fun LeaveRequestButton() {
    Button(
        onClick = {
            NetworkManager.leave()
        },
        colors = ButtonDefaults.buttonColors(backgroundColor = Color.Green),
    ) { Text("Leave Network") }
}