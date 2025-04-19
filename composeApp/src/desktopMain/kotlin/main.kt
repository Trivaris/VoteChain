package com.trivaris.votechain

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowState
import androidx.compose.ui.window.application

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "VoteChain",
        state = WindowState(size = DpSize(540.dp, 960.dp))
    ) {
        App(
            darkTheme = false,
            dynamicColor = false
        )
    }
}