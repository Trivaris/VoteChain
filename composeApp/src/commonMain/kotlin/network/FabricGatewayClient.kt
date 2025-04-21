package com.trivaris.votechain.network

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.hyperledger.fabric.gateway.Contract
import org.hyperledger.fabric.gateway.Gateway
import org.hyperledger.fabric.gateway.Wallets
import java.nio.file.Paths

class FabricGatewayClient {

    private val wallet by lazy {
        Wallets.newFileSystemWallet(Paths.get("wallet"))
    }

    private val gateway: Gateway by lazy {
        val identity = wallet["appUser"]
            ?: throw IllegalStateException("Identity 'appUser' not found in wallet. Run enrollment first.")

        Gateway.createBuilder()
            .identity(wallet, "appUser")
            .networkConfig(Paths.get("connection.yaml"))
            .connect()
    }

    private val contract: Contract by lazy {
        val network = gateway.getNetwork("mychannel")
        network.getContract("MyAssetContract")
    }

    suspend fun submitTransaction(txName: String, vararg args: String): String =
        withContext(Dispatchers.IO) {
            contract.submitTransaction(txName, *args).toString(Charsets.UTF_8)
        }

    suspend fun evaluateTransaction(txName: String, vararg args: String): String =
        withContext(Dispatchers.IO) {
            contract.evaluateTransaction(txName, *args).toString(Charsets.UTF_8)
        }
}
