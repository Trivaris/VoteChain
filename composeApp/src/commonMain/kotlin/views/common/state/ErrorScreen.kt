package com.trivaris.votechain.views.common.state

import androidx.compose.foundation.layout.Box
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment

@Composable
fun ErrorScreen(message: String? = null) {
    Box(
        contentAlignment = Alignment.Center
    ) {
        Text(text = message ?: "An error occured")
    }
}