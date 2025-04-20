package com.trivaris.votechain.models.candidate

import io.realm.kotlin.internal.platform.appFilesDirectory

actual fun getRealmDirectory(): String {
    return appFilesDirectory() + "/store/"
}