package com.trivaris.votechain.view.votingview

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.width
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ExposedDropdownMenuBox
import androidx.compose.material.ExposedDropdownMenuDefaults
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.trivaris.votechain.vote.Candidate

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun CandidateSelector(onCandidateSelected: (Candidate) -> Unit) {
    val options: List<Candidate> = Candidate.entries
    var expanded by remember { mutableStateOf(false) }
    var selectedOption by remember { mutableStateOf(options[0]) }
    val textFieldState = remember { mutableStateOf(TextFieldValue(selectedOption.readableName)) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = it },
        modifier = Modifier.width(200.dp)
    ) {
        OutlinedTextField(
            value = textFieldState.value,
            onValueChange = { newText -> textFieldState.value = newText },
            label = { Text("Select Candidate") },
            enabled = false,
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
            },
            colors = TextFieldDefaults.outlinedTextFieldColors(
                disabledTextColor = Color.White,
                disabledBorderColor = Color.Green,
                disabledLabelColor = Color.Green,
                focusedBorderColor = Color.Green,
                unfocusedBorderColor = Color.White,
                cursorColor = Color.Green,
                unfocusedLabelColor = Color.Green,
                focusedLabelColor = Color.White
            )
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.background(Color.White)
        ) {
            options.forEach { candidate ->
                DropdownMenuItem(
                    onClick = {
                        selectedOption = candidate
                        onCandidateSelected(candidate)
                        textFieldState.value = TextFieldValue(candidate.readableName)
                        expanded = false
                    }
                ) {
                    Text(text = candidate.readableName)
                }
            }
        }
    }
}
