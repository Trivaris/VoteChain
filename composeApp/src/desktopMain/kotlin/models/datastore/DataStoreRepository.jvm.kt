package com.trivaris.votechain.models.datastore

import java.io.File

actual fun getDataStoreDirectory(): String {
    return File("./store/datastore/$DATA_STORE_FILE_NAME").absolutePath
}