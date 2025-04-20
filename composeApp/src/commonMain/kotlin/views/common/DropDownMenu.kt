package com.trivaris.votechain.views.common

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.trivaris.votechain.theme.VoteChainTypography
import androidx.compose.material3.ExposedDropdownMenuDefaults


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun <T> DropDownMenu(
    options: Map<T, String>,
    selectedOption: T?,
    label: @Composable () -> Unit,
    optionsEmptyMessage: String,
    onOptionSelected: (T) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = it },
    ) {
        OutlinedTextField(
            value = options[selectedOption] ?: "",
            onValueChange = { },
            enabled = true,
            readOnly = true,
            label = label,
            colors = ExposedDropdownMenuDefaults.textFieldColors(
                focusedContainerColor = MaterialTheme.colorScheme.surface,
                unfocusedContainerColor = MaterialTheme.colorScheme.surface
            ),
            textStyle = VoteChainTypography.labelLarge,
            modifier = Modifier
                .width(280.dp)
                .menuAnchor(MenuAnchorType.PrimaryEditable)
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
        ) {
            if (options.isEmpty()) {
                DropdownMenuItem(
                    text = { Text(optionsEmptyMessage) },
                    onClick = {},
                    contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding
                )
            }

            options.forEach { option ->
                DropdownMenuItem(
                    text = { Text(option.value) },
                    onClick = {
                        onOptionSelected(option.key)
                        expanded = false
                    },
                    contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding
                )
            }
        }
    }
}