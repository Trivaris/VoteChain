package com.trivaris.votechain.app

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResultLauncher
import com.journeyapps.barcodescanner.ScanContract
import com.journeyapps.barcodescanner.ScanOptions
import com.trivaris.votechain.Config
import com.trivaris.votechain.blockchain.SerializableKeyPair
import com.trivaris.votechain.config
import com.trivaris.votechain.networking.NetworkManager
import com.trivaris.votechain.voting.VotingManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import java.net.Inet4Address
import java.net.NetworkInterface

class MainActivity : ComponentActivity() {

    private val barcodeLauncher: ActivityResultLauncher<ScanOptions> =
        registerForActivityResult(ScanContract()) { result ->
            if (result.contents == null) {
                Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show()
            } else {
                val scannedContent = result.contents
                val keyPair = Json.decodeFromString<SerializableKeyPair>(scannedContent).getKeyPair()
                Toast.makeText(this, "Successfully Scanned Keypair!", Toast.LENGTH_LONG).show()
                VotingManager.setKeypair(keyPair)
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        config = Config("")
        CoroutineScope(Dispatchers.IO).launch {
            val localip = getLocalIpAddress()
            if (localip == "")
                Toast.makeText(this@MainActivity, "Did not find local ip...", Toast.LENGTH_LONG).show()
            else
                Toast.makeText(this@MainActivity, "Found local ip: $localip", Toast.LENGTH_LONG).show()
            NetworkManager.join(localip)
        }

        setContent {
            AndroidApp(barcodeLauncher)
        }

    }
}

fun getLocalIpAddress(): String {
    return try {
        val interfaces = NetworkInterface.getNetworkInterfaces()
        interfaces.asSequence()
            .flatMap { it.inetAddresses.asSequence() }
            .firstOrNull { it is Inet4Address && !it.isLoopbackAddress }
            ?.hostAddress.toString()
    } catch (e: Exception) {
        ""
    }
}
