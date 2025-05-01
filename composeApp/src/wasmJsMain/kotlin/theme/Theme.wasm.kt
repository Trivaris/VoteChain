package com.trivaris.votechain.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import com.trivaris.votechain.models.datastore.PreferenceKeys
import kotlinx.coroutines.flow.Flow

@Composable
actual fun AppTheme(
    darkTheme: Flow<Boolean>,
    materialTheme: Flow<Boolean>,
    content: @Composable () -> Unit
) {
    val isDarkTheme by darkTheme.collectAsState(
        initial = PreferenceKeys.DARK_MODE.defaultValue
    )

    val colorScheme = remember(isDarkTheme) {
        if (isDarkTheme)
            darkScheme
        else lightScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = VoteChainTypography,
        content = content
    )
}