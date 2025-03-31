package com.trivaris.votechain.app.votingview

import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.trivaris.votechain.Config
import com.trivaris.votechain.networking.MessageManager
import com.trivaris.votechain.networking.Message
import com.trivaris.votechain.networking.MessageType
import java.net.InetAddress

@Composable
fun KeysRequestButton() {
    Button(
        onClick = {
            val message = Message(MessageType.KEYS_REQUEST, data = "GET")
            MessageManager.outgoing(message, InetAddress.getByName(Config.data.serverIP))
        },
        colors = ButtonDefaults.buttonColors(backgroundColor = Color.Green),
    ) { Text("Request Keys") }
}