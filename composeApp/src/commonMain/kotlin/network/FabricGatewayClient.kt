package com.trivaris.votechain.network

import com.trivaris.votechain.crypto.IdentityCreator
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.hyperledger.fabric.gateway.Contract
import org.hyperledger.fabric.gateway.Gateway
import org.hyperledger.fabric.gateway.Wallets
import java.io.File
import java.nio.file.Paths

expect fun getWalletPath(): String
expect fun getNetworkConfigPath(): String

private const val username: String = "Default"

class FabricGatewayClient {

    private val wallet by lazy {
        Wallets.newFileSystemWallet(Paths.get(getWalletPath()))
    }

    private val identity by lazy {
        wallet.get(username) ?:
            IdentityCreator().makeIdentity().also {
                wallet.put(username, it)
            }
    }

    private val gateway: Gateway by lazy {
        Gateway.createBuilder()
            .identity(identity)
            .networkConfig(Paths.get(getNetworkConfigPath()))
            .connect()
    }

    val contract: Contract by lazy {
        val network = gateway.getNetwork("votechain")
        network.getContract(VoteChainContract::class.simpleName)
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