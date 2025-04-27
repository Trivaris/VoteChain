package com.trivaris.votechain.network

import java.io.File

actual fun getWalletPath(): String {
    return File("./store/wallet/").absolutePath
}
actual fun getNetworkConfigPath(): String {
    return File("./store/wallet/connection.yaml").absolutePath
}