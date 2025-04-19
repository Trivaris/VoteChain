package com.trivaris.votechain.views.counting

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions
import com.trivaris.votechain.views.common.BottomNavBar
import com.trivaris.votechain.views.common.TopLogoBar
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import votechain.composeapp.generated.resources.*
import votechain.composeapp.generated.resources.Res
import votechain.composeapp.generated.resources.counting_tab

object CountingTab : Tab {

    @Composable
    override fun Content() {
        Scaffold(
            topBar = {
                TopLogoBar(stringResource(Res.string.counting_tab))
            },
            bottomBar = {
                BottomNavBar()
            }
        ) { padding ->

        }
    }

    override val options: TabOptions
        @Composable get() {
            val title = stringResource(Res.string.counting_tab)
            val icon = painterResource(Res.drawable.counting_tab_icon)

            return remember {
                TabOptions(
                    index = 0u,
                    title = title,
                    icon = icon
                )
            }
        }
}