package com.trivaris.votechain.app

import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.ui.platform.LocalContext
import com.journeyapps.barcodescanner.ScanContract
import com.trivaris.votechain.common.Config
import com.trivaris.votechain.blockchain.BlockDatabaseManager
import com.trivaris.votechain.network.SerializableKeyPair
import com.trivaris.votechain.blockchain.database.DriverFactory
import com.trivaris.votechain.network.NetworkManager
import com.trivaris.votechain.vote.VotingManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import java.io.File
import java.net.Inet4Address
import java.net.NetworkInterface

class MainActivity : ComponentActivity() {

    private val barcodeLauncher = registerForActivityResult(ScanContract()) { result ->
        result.contents?.let {
            try {
                val keyPair = Json.decodeFromString<SerializableKeyPair>(it).getKeyPair()
                Toast.makeText(this, "Successfully Scanned Keypair!", Toast.LENGTH_LONG).show()
                VotingManager.setKeypair(keyPair)
            } catch (e: Exception) {
                Toast.makeText(this, "Invalid QR Code", Toast.LENGTH_LONG).show()
            }
        } ?: Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Config.isAndroid = true

        val configJson = initializeConfig(this)
        val configFile = File(this.filesDir, "config.json")

        Config.data.printHashCalc = false

        Config.setFile(configFile)
        Config.setSource(configJson)


        val driverFactory = DriverFactory(this)
        BlockDatabaseManager.initDatabase(driverFactory)

        fetchLocalIpAndJoinNetwork()

        setContent {
            val context = LocalContext.current
            StdOutToastListener(context)
            App(
                loadKeysButton = { QRScannerButton(barcodeLauncher) },
                onSettingsSaved = {
                    Toast.makeText(context, "Saved Settings!", Toast.LENGTH_LONG).show()
                },
                onVoteFailed = {
                    Toast.makeText(context, "You did not load your keys yet!", Toast.LENGTH_LONG).show()
                }
            )
        }
    }

    private fun fetchLocalIpAndJoinNetwork() {
        CoroutineScope(Dispatchers.IO).launch {
            val localIp = getLocalIpAddress()
            withContext(Dispatchers.Main) {
                if (localIp.isNotEmpty())
                    NetworkManager.join(localIp)
            }
        }
    }
}

fun initializeConfig(context: Context): String {
    return try {
        val file = File(context.filesDir, "config.json")
        if (!file.exists()) file.createNewFile()
        file.readText()
    } catch (e: Exception) {
        e.printStackTrace()
        ""
    }
}

fun getLocalIpAddress(): String {
    return try {
        NetworkInterface.getNetworkInterfaces().asSequence()
            .flatMap { it.inetAddresses.asSequence() }
            .filterIsInstance<Inet4Address>()
            .firstOrNull { !it.isLoopbackAddress }
            ?.hostAddress.orEmpty()
    } catch (e: Exception) {
        ""
    }
}
