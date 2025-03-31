package com.trivaris.votechain.app

import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Color
import com.trivaris.votechain.blockchain.BlockManager
import com.trivaris.votechain.networking.Message
import com.trivaris.votechain.networking.MessageManager

@Composable
fun BlockMineButton() {
    Button(
        onClick = {
            val block = BlockManager.makeNewestBlock()
            val message = Message(block)
            MessageManager.outgoing(message)
        },
        colors = ButtonDefaults.buttonColors(backgroundColor = Color.Green)
    ) { Text("Mine new Block and send") }
}