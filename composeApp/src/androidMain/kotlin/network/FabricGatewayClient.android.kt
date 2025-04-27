package com.trivaris.votechain.network

import io.realm.kotlin.internal.platform.appFilesDirectory

actual fun getWalletPath(): String {
    return appFilesDirectory() + "/store/wallet/"
}
actual fun getNetworkConfigPath(): String {
    return appFilesDirectory() + "/store/wallet/connection.yaml"
}