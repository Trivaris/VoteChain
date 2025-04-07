package com.trivaris.votechain.app.votingview

import androidx.compose.runtime.Composable
import com.trivaris.votechain.Config
import com.trivaris.votechain.blockchain.SerializableKeyPair
import com.trivaris.votechain.decodeQRCode
import com.trivaris.votechain.voting.VotingManager

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