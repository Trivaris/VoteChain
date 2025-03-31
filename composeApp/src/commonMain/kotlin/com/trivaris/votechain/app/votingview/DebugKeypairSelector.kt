package com.trivaris.votechain.app.votingview

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.trivaris.votechain.debugKeypairs
import com.trivaris.votechain.voting.VotingManager

@Composable
fun DebugKeypairSelector() {
    val selectedNumber = remember {mutableStateOf(1)}
    Row {
        NumberDropdown(selectedNumber) {
            selectedNumber.value = it
        }
        Spacer(modifier = Modifier.width(8.dp))
        Button(onClick = {
            VotingManager.setKeypair(debugKeypairs[selectedNumber.value])
        }, colors = ButtonDefaults.buttonColors(backgroundColor = Color.Green)) { Text("Set Debug Keypair") }
    }
}

@Composable
private fun NumberDropdown(selectedNumber: MutableState<Int>, onNumberSelected: (Int) -> Unit) {
    var expanded by remember { mutableStateOf(false) }

    Box {
        Button(onClick = { expanded = true }, colors = ButtonDefaults.buttonColors(backgroundColor = Color.Green)) {
            Text(selectedNumber.value.toString())
        }
        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            (1..10).forEach { number ->
                DropdownMenuItem(onClick = {
                    onNumberSelected(number)
                    expanded = false
                }) { Text(number.toString()) }
            }
        }
    }
}
