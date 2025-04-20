package com.trivaris.votechain.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf

@Composable
actual fun AppTheme(
    darkTheme: Boolean,
    dynamicColor: Boolean,
    content: @Composable () -> Unit
) {
    val colorScheme by mutableStateOf(
        when {
            darkTheme -> darkScheme
            else -> lightScheme
        }
    )

    MaterialTheme(
        colorScheme = colorScheme,
        typography = VoteChainTypography,
        content = content
    )
}