package com.trivaris.votechain.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import kotlinx.coroutines.flow.Flow

@Composable
actual fun AppTheme(
    darkTheme: Flow<Boolean>,
    materialTheme: Flow<Boolean>,
    content: @Composable () -> Unit
) {
  val colorScheme = when {
      darkTheme -> darkScheme
      else -> lightScheme
  }

  MaterialTheme(
    colorScheme = colorScheme,
    typography = VoteChainTypography,
    content = content
  )
}