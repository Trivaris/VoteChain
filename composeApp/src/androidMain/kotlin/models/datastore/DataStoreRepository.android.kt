package com.trivaris.votechain.models.datastore

import io.realm.kotlin.internal.platform.appFilesDirectory

actual val dataStoreDirectory: String
    get() {
        return appFilesDirectory() + "/store/datastore/$DATA_STORE_FILE_NAME"
    }