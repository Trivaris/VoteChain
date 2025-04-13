package com.trivaris.votechain.view.votingview

import androidx.compose.runtime.Composable
import com.trivaris.votechain.common.Config
import com.trivaris.votechain.network.SerializableKeyPair
import com.trivaris.votechain.common.decodeQRCode
import com.trivaris.votechain.vote.VotingManager

@Composable
fun KeyFileLoader() {
    FileLoader(
        onFileSelected = { file ->
            val json = file.decodeQRCode()
            val keypair = Config.json.decodeFromString<SerializableKeyPair>(json)
            VotingManager.setKeypair(keypair.getKeyPair())
        }
    )
}