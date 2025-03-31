package com.trivaris.votechain.app

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.trivaris.votechain.Config
import kotlinx.serialization.json.Json

val json = Json { prettyPrint = true }

@Composable
fun SettingsScreen(onSave: () -> Unit = {}) {
    val configState = remember { mutableStateOf(Config.data) }

    Column(modifier = Modifier.padding(16.dp).fillMaxWidth()) {
        Row {
            Text(text = "Se", color = Color.Green, fontWeight = FontWeight.Medium, fontSize = 24.sp)
            Text(text = "ttings", color = Color.White, fontWeight = FontWeight.ExtraBold, fontSize = 24.sp)
        }
        Spacer(Modifier.height(16.dp))

        IntOption("Difficulty",     configState.value.difficulty)       { configState.value = configState.value.copy(difficulty = it) }
        IntOption("Keypair Amount", configState.value.keypairAmount)    { configState.value = configState.value.copy(keypairAmount = it) }
        StringOption("Server IP",   configState.value.serverIP)         { configState.value = configState.value.copy(serverIP = it) }

        BooleanOption("Print Hash Calculation", configState.value.printHashCalc) { configState.value = configState.value.copy(printHashCalc = it) }
        BooleanOption("Debug Mode", configState.value.debugMode)        { configState.value = configState.value.copy(debugMode = it) }
        BooleanOption("Is Server",  configState.value.isServer)         { configState.value = configState.value.copy(isServer = it) }

        Spacer(Modifier.height(16.dp))
        Button(
            onClick = {
                Config.data = configState.value
                println(json.encodeToString(configState.value))
                Config.save()
                onSave()
            },
            colors = ButtonDefaults.buttonColors(backgroundColor = Color.Green))
        {
            Text("Save Settings")
        }
    }
}


@Composable
fun StringOption(label: String, value: String, onValueChange: (String) -> Unit) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        modifier = Modifier.fillMaxWidth(),
        colors = TextFieldDefaults.outlinedTextFieldColors(
            focusedBorderColor = Color.Green,
            unfocusedBorderColor = Color.Gray,
            cursorColor = Color.Green,
            unfocusedLabelColor = Color.Green,
            focusedLabelColor = Color.White,
            textColor = Color.White
        )
    )
}

@Composable
fun IntOption(label: String, value: Int, onValueChange: (Int) -> Unit) {
    StringOption(label, value.toString()) {
        onValueChange(it.toIntOrNull() ?: value)
    }
}

@Composable
fun BooleanOption(label: String, value: Boolean, onValueChange: (Boolean) -> Unit) {
    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
        Checkbox(checked = value, onCheckedChange = onValueChange, colors = CheckboxDefaults.colors(
            checkedColor = Color.Green,
            uncheckedColor = Color.White,
            checkmarkColor = Color.White
        ))
        Text(label, modifier = Modifier.padding(start = 8.dp), color = Color.White)
    }
}