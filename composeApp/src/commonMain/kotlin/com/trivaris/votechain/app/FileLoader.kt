package com.trivaris.votechain.app

import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.font.FontWeight
import java.awt.FileDialog
import java.awt.Frame
import java.awt.image.BufferedImage
import java.io.File
import javax.imageio.ImageIO

@Composable
fun FileLoader(onFileSelected: (File) -> Unit) {
    var text by remember { mutableStateOf("") }
    var selectedFile by remember { mutableStateOf<File?>(null) }

    fun onIconClick() {
        val fileDialog = FileDialog(Frame(), "Select a File", FileDialog.LOAD)
        fileDialog.isVisible = true

        val selectedFilePath = fileDialog.file
        val directory = fileDialog.directory
        if (selectedFilePath != null) {
            val file = File(directory, selectedFilePath)
            selectedFile = file
            onFileSelected(file)
            text = file.name
        }
    }

    OutlinedTextField(
        value = text,
        onValueChange = { text = it },
        label = { Text(text = "Public Key", fontWeight = FontWeight.Bold,) },
        leadingIcon = {
            IconButton(onClick = { onIconClick() }) {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Open File Chooser",
                    tint = Color.Green
                )
            }
        },
        modifier = Modifier
            .padding(16.dp),
        enabled = false,
        colors = TextFieldDefaults.outlinedTextFieldColors(
            focusedBorderColor = Color.Green,
            unfocusedBorderColor = Color.White,
            cursorColor = Color.Green,
            unfocusedLabelColor = Color.Green,
            focusedLabelColor = Color.White,
            textColor = Color.White
        ),
    )
}