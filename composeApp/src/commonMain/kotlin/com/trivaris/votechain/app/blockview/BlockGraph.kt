package com.trivaris.votechain.app.blockview

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.trivaris.votechain.store.block.BlockObject
import com.trivaris.votechain.Config
import com.trivaris.votechain.Logger
import com.trivaris.votechain.app.Logo
import com.trivaris.votechain.store.block.BlockRepository
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

val sdf = SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.getDefault())

@Composable
fun BlockGraph() {
    Logger.DEBUG.log("Starting BlockGraph")
    val blocks = BlockRepository.getBlocks().values.toList()

    Spacer(modifier = Modifier.height(48.dp))

    if (blocks.isEmpty()) {
        Logo()
        Text("No blocks found", color = Color.White)
    }
    else
    LazyColumn(modifier = Modifier.fillMaxSize()) {
        items(blocks) { block ->
            Block(block)
            Spacer(modifier = Modifier.height(16.dp)) // Constant distance between blocks
        }
    }
}
@Composable
fun Block(block: BlockObject) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.DarkGray, shape = RoundedCornerShape(16.dp))
            .border(2.dp, Color.Green, shape = RoundedCornerShape(16.dp))
            .padding(16.dp)
    ) {
        Column {
            Row(modifier = Modifier.fillMaxWidth()) {
                val hashPrefix = block.hash.take(Config.data.difficulty)
                val remainingHash = block.hash.drop(Config.data.difficulty).take(10) + "..."

                Text(
                    text = hashPrefix,
                    fontSize = 32.sp,
                    color = Color.Green,
                    fontWeight = FontWeight.ExtraBold
                )
                Text(
                    text = remainingHash,
                    fontSize = 32.sp,
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
            }
            Spacer(modifier = Modifier.height(8.dp))

            Property("Votes: ", if (block.votes.isNotEmpty()) block.votes.toString() else "Empty!")
            Property("Previous Hash: ", block.previousHash.take(10) + "...")
            Property("Timestamp: ", sdf.format(Date(block.timestamp)))
            Property("Validity: ", block.validity() ?: "Valid")

            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Composable
fun Property(name: String, value: String) {
    Row {
        Text(
            text = name,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = value,
            fontSize = 20.sp
        )
    }
    Spacer(modifier = Modifier.height(4.dp))
}