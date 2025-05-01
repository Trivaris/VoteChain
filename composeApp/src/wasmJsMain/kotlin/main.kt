package com.trivaris.votechain

import com.trivaris.votechain.di.dataStoreModule
import com.trivaris.votechain.di.votingModule
import org.koin.core.context.startKoin
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.ComposeViewport
import kotlinx.browser.document

@OptIn(ExperimentalComposeUiApi::class)
fun main() {
    startKoin {
        modules(
            dataStoreModule,
            votingModule,
        )
    }

    ComposeViewport(document.body!!) {
        App()
    }
}