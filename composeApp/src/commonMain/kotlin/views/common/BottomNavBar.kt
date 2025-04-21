package com.trivaris.votechain.views.common

import androidx.compose.foundation.layout.RowScope
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.runtime.Composable
import cafe.adriel.voyager.navigator.tab.LocalTabNavigator
import cafe.adriel.voyager.navigator.tab.Tab
import com.trivaris.votechain.views.BlockChainTab
import com.trivaris.votechain.views.CountingTab
import com.trivaris.votechain.views.SettingsTab
import com.trivaris.votechain.views.VotingTab

@Composable
fun BottomNavBar() {
    NavigationBar {
        TabNavigationItem(CountingTab)
        TabNavigationItem(VotingTab)
        TabNavigationItem(BlockChainTab)
        TabNavigationItem(SettingsTab)
    }
}

@Composable
private fun RowScope.TabNavigationItem(tab: Tab) {
    val tabNavigator = LocalTabNavigator.current

    NavigationBarItem(
        selected = tabNavigator.current == tab,
        onClick = { tabNavigator.current = tab },
        icon = { Icon(tab.options.icon!!, contentDescription = tab.options.title) }
    )
}