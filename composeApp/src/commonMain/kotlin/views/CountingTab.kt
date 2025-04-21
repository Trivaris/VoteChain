package com.trivaris.votechain.views

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.koin.getScreenModel
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions
import com.trivaris.votechain.models.RequestState
import com.trivaris.votechain.models.vote.VoteResult
import com.trivaris.votechain.viewmodels.CountingViewModel
import com.trivaris.votechain.views.common.BottomNavBar
import com.trivaris.votechain.views.common.TopLogoBar
import com.trivaris.votechain.views.common.state.ErrorScreen
import com.trivaris.votechain.views.common.state.LoadingScreen
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import votechain.composeapp.generated.resources.*
import votechain.composeapp.generated.resources.Res
import votechain.composeapp.generated.resources.counting_tab

object CountingTab : Tab {
    private fun readResolve(): Any = CountingTab

    @Composable
    override fun Content() {
        val viewModel = getScreenModel<CountingViewModel>()

        Scaffold(
            topBar = {
                TopLogoBar(stringResource(Res.string.counting_tab))
            },
            bottomBar = {
                BottomNavBar()
            }
        ) { padding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .wrapContentSize(Alignment.TopCenter),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                VoteResults(viewModel.results.value)
                Button(onClick = { viewModel.refresh() }) {
                    Text(stringResource(Res.string.refresh_results))
                }
            }
        }
    }

    @Composable
    private fun VoteResults(
        resultState: RequestState<List<VoteResult>>
    ) {
        resultState.DisplayResult(
            onLoading = { LoadingScreen() },
            onError = { ErrorScreen() },
            onSuccess = { results ->
                LazyColumn {
                    items(results.size) { index ->
                        Text("${results[index].candidateId}: ${results[index].candidateId}")
                    }
                }
            }
        )
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
