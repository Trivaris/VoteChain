package com.trivaris.votechain

import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowState
import androidx.compose.ui.window.application
import com.trivaris.votechain.di.commonModule
import com.trivaris.votechain.di.mongoModule
import com.trivaris.votechain.di.dataStoreModule
import org.koin.core.context.startKoin

fun main() {
    startKoin {
        modules(
            commonModule,
            dataStoreModule,
            mongoModule
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