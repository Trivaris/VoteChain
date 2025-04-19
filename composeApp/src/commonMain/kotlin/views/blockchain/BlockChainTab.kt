package com.trivaris.votechain.views.blockchain

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
import votechain.composeapp.generated.resources.blockchain_tab
import votechain.composeapp.generated.resources.blockchain_tab_icon

object BlockChainTab : Tab {

    @Composable
    override fun Content() {
        Scaffold(
            topBar = {
                TopLogoBar(stringResource(Res.string.blockchain_tab))
            },
            bottomBar = {
                BottomNavBar()
            }
        ) { padding ->

        }
    }

    override val options: TabOptions
        @Composable get() {
            val title = stringResource(Res.string.blockchain_tab)
            val icon = painterResource(Res.drawable.blockchain_tab_icon)

            return remember {
                TabOptions(
                    index = 2u,
                    title = title,
                    icon = icon
                )
            }
        }
}