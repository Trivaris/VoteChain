package com.trivaris.votechain.view

import androidx.compose.foundation.layout.Row
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

@Composable
fun Logo() {
    Row {
        Text(text = "Vote", color = Color.Green, fontWeight = FontWeight.Medium, fontSize = 24.sp)
        Text(text = "Chain", color = Color.White, fontWeight = FontWeight.ExtraBold, fontSize = 24.sp)
    }
}