package com.trivaris.votechain.view.votingview

import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.trivaris.votechain.network.NetworkManager

@Composable
fun KeysRequestButton() {
    Button(
        onClick = {
            NetworkManager.requestKeys()
        },
        colors = ButtonDefaults.buttonColors(backgroundColor = Color.Green),
    ) { Text("Request Keys") }
}