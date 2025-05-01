package com.trivaris.votechain.models.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import okio.Path.Companion.toPath
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

const val DATA_STORE_FILE_NAME = "prefs.preferences_pb"

expect val dataStoreDirectory: String

object DataStoreClient : KoinComponent {
    val repo: DataStoreRepository by inject()
}

class DataStoreRepository {
    private var dataStore: DataStore<Preferences>? = null

    private val dataStoreInstance: DataStore<Preferences>
        get() {
            if (dataStore == null)
                configureDataStore()
            return dataStore!!
        }

    private fun configureDataStore() {
        dataStore = PreferenceDataStoreFactory.createWithPath(
            produceFile = { dataStoreDirectory.toPath() }
        )
    }

    fun <T>readPref(preference: Preference<T>): Flow<T> {
        return dataStoreInstance.data
            .map { it[preference.key] ?: preference.defaultValue }
    }

    suspend fun <T>editPref(preference: Preference<T>, value: T) {
        dataStoreInstance.edit { it[preference.key] = value }
    }
}