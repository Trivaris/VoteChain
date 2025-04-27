package com.trivaris.votechain.views

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.koin.getScreenModel
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions
import com.trivaris.votechain.models.RequestState
import com.trivaris.votechain.models.candidate.CandidateObject
import com.trivaris.votechain.models.datastore.DataStoreClient
import com.trivaris.votechain.models.datastore.PreferenceKeys
import com.trivaris.votechain.viewmodels.VotingViewModel
import com.trivaris.votechain.views.common.BottomNavBar
import com.trivaris.votechain.views.common.DropDownMenu
import com.trivaris.votechain.views.common.TopLogoBar
import com.trivaris.votechain.views.common.state.ErrorScreen
import com.trivaris.votechain.views.common.state.LoadingScreen
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import votechain.composeapp.generated.resources.Res
import votechain.composeapp.generated.resources.voting_tab
import votechain.composeapp.generated.resources.voting_tab_icon
import org.jetbrains.compose.ui.tooling.preview.Preview
import votechain.composeapp.generated.resources.select_candidate

object VotingTab : Tab {
    private fun readResolve(): Any = VotingTab

    @Preview
    @Composable
    override fun Content() {
        val viewModel = getScreenModel<VotingViewModel>()

        Scaffold(
            topBar = {
                TopLogoBar(stringResource(Res.string.voting_tab))
            },
            bottomBar = {
                BottomNavBar()
            },
            floatingActionButton = {
                FloatingActionButton(
                    onClick = {
                        viewModel.castVote()
                    }
                ) {
                    Icon(
                        Icons.AutoMirrored.Default.Send,
                        contentDescription = "Send"
                    )
                }
            }
        ) { padding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .wrapContentSize(Alignment.TopCenter),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                CandidateDropDown(
                    viewModel.candidates.value,
                    viewModel.selectedCandidate.value
                ) { candidate ->
                    viewModel.setSelectedCandidate(candidate)
                }
                Text(DataStoreClient.repo.readPref(PreferenceKeys.VOTER_ID).collectAsState("").value)
            }
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

    @Composable
    private fun CandidateDropDown(
        candidates: RequestState<List<CandidateObject>>,
        selectedCandidate: CandidateObject?,
        onCandidateSelected: (CandidateObject) -> Unit
    ) {
        candidates.DisplayResult(
            onLoading = { LoadingScreen() },
            onError = { ErrorScreen() },
            onSuccess = {
                DropDownMenu(
                    options = it.associateWith { candidate -> candidate.getFullName() },
                    selectedOption = selectedCandidate,
                    label = { Text(stringResource(Res.string.select_candidate)) },
                    "No Candidates found",
                    onCandidateSelected
                )
            }
        )
    }
}