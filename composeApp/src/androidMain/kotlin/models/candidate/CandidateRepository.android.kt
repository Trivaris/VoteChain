package com.trivaris.votechain.models.candidate

import io.realm.kotlin.internal.platform.appFilesDirectory

actual fun getCandidatesDirectory(): String {
    return appFilesDirectory() + "/store/candidate/"
}