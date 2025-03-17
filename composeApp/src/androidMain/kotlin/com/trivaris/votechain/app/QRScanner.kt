package com.trivaris.votechain.app

import androidx.activity.result.ActivityResultLauncher
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.journeyapps.barcodescanner.ScanOptions

@Composable
fun QRScannerButton(barcodeLauncher: ActivityResultLauncher<ScanOptions>) {
    Button(
        onClick = {
            val options = ScanOptions().apply {
                setDesiredBarcodeFormats(ScanOptions.QR_CODE)
                setCameraId(0)
                setBeepEnabled(false)
                setBarcodeImageEnabled(true)
                setOrientationLocked(false)
            }
            barcodeLauncher.launch(options)
        },
        colors = ButtonDefaults.buttonColors(backgroundColor = Color.Green),
    ) {
        Text("Scan Keypair")
    }
}