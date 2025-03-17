package com.trivaris.votechain.app

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.trivaris.votechain.blockchain.BlockStorage
import com.trivaris.votechain.config
import com.trivaris.votechain.voting.VotingManager

@Composable
fun DesktopApp() {
    val allVotes = remember { mutableStateOf(VotingManager.countVotes()) }
    var showSettings by remember { mutableStateOf(false) }
    var showGraph by remember { mutableStateOf(false) }

    Box(Modifier.fillMaxSize().background(Color(42, 42, 42))) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            horizontalArrangement = Arrangement.End
        ) {
            IconButton(onClick = { showSettings = !showSettings }) {
                Icon(Icons.Default.Settings, contentDescription = "Settings", tint = Color.White)
            }
            IconButton(onClick = { showGraph = !showGraph }) {
                Icon(Icons.Default.Search, contentDescription = "Graph", tint = Color.White)
            }
        }
        Column(
            modifier = Modifier.fillMaxWidth().height(1300.dp).padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceEvenly
        ) {
            if (showSettings) {
                SettingsScreen()
            } else if (showGraph) {
                BlockGraph()
            } else {
                Logo()
                KeySelector()
                Voting()
                if (config!!.data.debugMode) KeysRequestButton()
                BlockMineButton()
                CountVotesButton(allVotes)
                CandidateVotes(votes = allVotes.value)
            }
        }
    }
}