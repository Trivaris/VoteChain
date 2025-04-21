package com.trivaris.votechain.views

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.koin.getScreenModel
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions
import com.trivaris.votechain.viewmodels.SettingsViewModel
import com.trivaris.votechain.views.common.BottomNavBar
import com.trivaris.votechain.views.common.TopLogoBar
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import votechain.composeapp.generated.resources.*
import votechain.composeapp.generated.resources.Res

object SettingsTab : Tab {
    private fun readResolve(): Any = SettingsTab

    @Composable
    override fun Content() {
        val viewModel = getScreenModel<SettingsViewModel>()

        Scaffold(
            topBar = {
                TopLogoBar(stringResource(Res.string.settings_tab))
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
                BoolOption(
                    label = stringResource(Res.string.pref_dark_mode),
                    checked = viewModel.darkMode.value,
                    onCheckedChange = viewModel::setDarkMode
                )
                BoolOption(
                    label = stringResource(Res.string.pref_dynamic_color),
                    checked = viewModel.dynamicColor.value,
                    onCheckedChange = viewModel::setDynamicColor
                )
                TextOption(
                    label = "Username",
                    value = viewModel.username.value,
                    onValueChange = viewModel::setUsername
                )
            }
        }
    }

    override val options: TabOptions
        @Composable get() {
            val title = stringResource(Res.string.settings_tab)
            val icon = painterResource(Res.drawable.settings_tab_icon)

            return remember {
                TabOptions(
                    index = 3u,
                    title = title,
                    icon = icon
                )
            }
        }

    @Composable
    private fun BoolOption(
        label: String,
        checked: Boolean,
        onCheckedChange: (Boolean) -> Unit,
        modifier: Modifier = Modifier
    ) {
        Row(
            modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp, horizontal = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = label, style = MaterialTheme.typography.bodyLarge)
            Switch(checked = checked, onCheckedChange = onCheckedChange)
        }
    }

    @Composable
    private fun TextOption(
        label: String,
        value: String,
        onValueChange: (String) -> Unit,
        modifier: Modifier = Modifier
    ) {
        Column(modifier.padding(vertical = 8.dp, horizontal = 16.dp)) {
            Text(text = label, style = MaterialTheme.typography.bodyLarge)
            Spacer(Modifier.height(4.dp))
            TextField(
                value = value,
                onValueChange = onValueChange,
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }

}