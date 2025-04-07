package com.trivaris.votechain.view.blockview

import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.trivaris.votechain.model.block.BlockRepository
import com.trivaris.votechain.networking.NetworkManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun BlockMineButton() {
    Button(
        onClick = {
            CoroutineScope(Dispatchers.IO).launch {
                val block = BlockRepository.makeNewestBlock()
                NetworkManager.broadcast(block)
            }
        },
        colors = ButtonDefaults.buttonColors(backgroundColor = Color.Green)
    ) { Text("Mine new Block and send") }
}