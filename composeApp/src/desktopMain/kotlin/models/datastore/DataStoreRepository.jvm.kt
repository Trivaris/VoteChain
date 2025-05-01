package com.trivaris.votechain.models.datastore

import java.io.File

actual val dataStoreDirectory: String
    get() {
        return File("./store/datastore/$DATA_STORE_FILE_NAME").absolutePath
    }