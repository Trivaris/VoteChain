package com.trivaris.votechain

import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import cafe.adriel.voyager.navigator.tab.CurrentTab
import cafe.adriel.voyager.navigator.tab.TabNavigator
import com.trivaris.votechain.models.candidate.CandidateRepository
import com.trivaris.votechain.theme.AppTheme
import com.trivaris.votechain.views.common.BottomNavBar
import com.trivaris.votechain.views.voting.VotingTab
import com.trivaris.votechain.views.voting.VotingViewModel
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.core.context.startKoin
import org.koin.dsl.module

@Composable
@Preview
fun App(
    darkTheme: Boolean,
    dynamicColor: Boolean
) {
    initKoin()

    AppTheme(
        darkTheme = darkTheme,
        dynamicColor = dynamicColor
    ) {
        TabNavigator(VotingTab) {
            Scaffold(
                content = {
                    CurrentTab()
                },
                bottomBar = {
                    BottomNavBar()
                }
            )
        }
    }
}

val mongoModule = module {
    single { CandidateRepository() }
    factory { VotingViewModel(get()) }
}

fun initKoin() {
    startKoin {
        modules(
            mongoModule
        )
    }
}