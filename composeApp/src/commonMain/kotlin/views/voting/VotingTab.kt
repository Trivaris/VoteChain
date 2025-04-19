package com.trivaris.votechain.views.voting

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions
import com.trivaris.votechain.views.common.BottomNavBar
import com.trivaris.votechain.views.common.TopLogoBar
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import votechain.composeapp.generated.resources.Res
import votechain.composeapp.generated.resources.voting_tab
import votechain.composeapp.generated.resources.voting_tab_icon

object VotingTab : Tab {

    @Composable
    override fun Content() {
        Scaffold(
            topBar = {
                TopLogoBar(stringResource(Res.string.voting_tab))
            },
            bottomBar = {
                BottomNavBar()
            }
        ) { padding ->

        }
    }

    override val options: TabOptions
        @Composable get() {
            val title = stringResource(Res.string.voting_tab)
            val icon = painterResource(Res.drawable.voting_tab_icon)

            return remember {
                TabOptions(
                    index = 1u,
                    title = title,
                    icon = icon
                )
            }
        }

}