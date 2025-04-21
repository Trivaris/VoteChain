package com.trivaris.votechain

import androidx.compose.material3.Scaffold
import cafe.adriel.voyager.navigator.tab.CurrentTab
import cafe.adriel.voyager.navigator.tab.TabNavigator
import com.trivaris.votechain.theme.AppTheme
import com.trivaris.votechain.views.common.BottomNavBar
import com.trivaris.votechain.views.VotingTab
import org.jetbrains.compose.ui.tooling.preview.Preview
import androidx.compose.runtime.Composable
import com.trivaris.votechain.models.datastore.DataStoreClient
import com.trivaris.votechain.models.datastore.PreferenceKeys

@Composable
@Preview
fun App() {
    AppTheme(
        DataStoreClient.repo.readPref(PreferenceKeys.DARK_MODE),
        DataStoreClient.repo.readPref(PreferenceKeys.DYNAMIC_COLOR)
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