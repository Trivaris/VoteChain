package com.trivaris.votechain

import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowState
import androidx.compose.ui.window.application
import com.trivaris.votechain.di.commonModule
import com.trivaris.votechain.di.candidateModule
import com.trivaris.votechain.di.dataStoreModule
import com.trivaris.votechain.di.voteModule
import org.koin.core.context.startKoin

fun main() {
    startKoin {
        modules(
            commonModule,
            dataStoreModule,
            candidateModule,
            voteModule,
        )
    }

    application {
        Window(
            onCloseRequest = ::exitApplication,
            title = "VoteChain",
            state = WindowState(size = DpSize(540.dp, 960.dp))
        ) {
            App()
        }
    }
}