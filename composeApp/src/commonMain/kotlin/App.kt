package com.trivaris.votechain

import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import cafe.adriel.voyager.navigator.tab.CurrentTab
import cafe.adriel.voyager.navigator.tab.TabNavigator
import com.trivaris.votechain.theme.AppTheme
import com.trivaris.votechain.views.common.BottomNavBar
import com.trivaris.votechain.views.voting.VotingTab
import org.jetbrains.compose.ui.tooling.preview.Preview


@Composable
@Preview
fun App(
    darkTheme: Boolean,
    dynamicColor: Boolean
) {
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