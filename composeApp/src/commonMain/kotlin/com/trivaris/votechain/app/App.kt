package com.trivaris.votechain.app

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
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
import com.trivaris.votechain.Config
import com.trivaris.votechain.Logger
import com.trivaris.votechain.app.blockview.BlockGraph
import com.trivaris.votechain.app.blockview.BlockMineButton
import com.trivaris.votechain.app.counterview.CandidateVotes
import com.trivaris.votechain.app.settingsview.SettingsScreen
import com.trivaris.votechain.app.votingview.DebugKeypairSelector
import com.trivaris.votechain.app.votingview.JoinRequestButton
import com.trivaris.votechain.app.votingview.KeysRequestButton
import com.trivaris.votechain.app.votingview.LeaveRequestButton
import com.trivaris.votechain.app.votingview.Voting
import com.trivaris.votechain.store.block.BlockRepository
import com.trivaris.votechain.networking.NetworkManager
import com.trivaris.votechain.voting.VotingManager

@Composable
fun App(
    loadKeysButton: @Composable () -> Unit,
    onVoteFailed: () -> Unit = {},
    onSettingsSaved: () -> Unit = {}
) {
    var currentScreen by remember { mutableStateOf(Screen.MAIN) }

    Box(Modifier.fillMaxSize().background(Color(42, 42, 42))) {
        TopBar(
            onSettingsClick = { currentScreen = toggleScreen(currentScreen, Screen.SETTINGS) },
            onGraphClick = { currentScreen = toggleScreen(currentScreen, Screen.BLOCKS_GRAPH) }
        )
        Spacer(modifier = Modifier.height(24.dp))
        ContentArea(currentScreen, loadKeysButton, onVoteFailed, onSettingsSaved)
    }
}

@Composable
private fun TopBar(onSettingsClick: () -> Unit, onGraphClick: () -> Unit) {
    Row(horizontalArrangement = Arrangement.Center,
        modifier = Modifier.offset(x = 16.dp, y = 16.dp)){ Logo() }
    Row(
        modifier = Modifier.fillMaxWidth().padding(16.dp),
        horizontalArrangement = Arrangement.End
    ) {
        if (Config.data.debugMode)
            IconButton(onClick = {
                Logger.DEBUG.log("Resetting Settings")
                NetworkManager.clearParticipants()
                BlockRepository.clear()
                VotingManager.clearCurrentVotes()
            }) {
                Icon(Icons.Default.Refresh, contentDescription = "Reset", tint = Color.White)
            }
        IconButton(onClick = onSettingsClick) {
            Icon(Icons.Default.Settings, contentDescription = "Settings", tint = Color.White)
        }
        IconButton(onClick = onGraphClick) {
            Icon(Icons.Default.Search, contentDescription = "Graph", tint = Color.White)
        }
    }
}

@Composable
private fun ContentArea(
    screen: Screen,
    loadKeysButton: @Composable () -> Unit = {},
    onVoteFailed: () -> Unit = {},
    onSettingsSaved: () -> Unit = {}
) {
    Column(
        modifier = Modifier.fillMaxWidth().height(1300.dp).padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceEvenly
    ) {
        when (screen) {
            Screen.SETTINGS -> SettingsScreen {
                onSettingsSaved()
            }
            Screen.BLOCKS_GRAPH -> BlockGraph()
            else -> MainContent(loadKeysButton, onVoteFailed)
        }
    }
}

@Composable
private fun MainContent(
    loadKeysButton: @Composable () -> Unit = {},
    onVoteFailed: () -> Unit = {}
) {

    Voting {
        onVoteFailed()
    }
    if (!Config.data.debugMode) loadKeysButton()
    else DebugKeypairSelector()
    Column {
        if (Config.data.debugMode)
            Row(horizontalArrangement = Arrangement.SpaceEvenly, modifier = Modifier.fillMaxWidth()) {
                JoinRequestButton()
                LeaveRequestButton()
            }
        Row(horizontalArrangement = Arrangement.SpaceEvenly, modifier = Modifier.fillMaxWidth()) {
            BlockMineButton()
            if(Config.data.debugMode) KeysRequestButton()
        }
    }
    CandidateVotes()
}

private fun toggleScreen(current: Screen, target: Screen) = if (current == target) Screen.MAIN else target

private enum class Screen {
    MAIN,
    SETTINGS,
    BLOCKS_GRAPH;
}