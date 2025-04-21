package com.trivaris.votechain.viewmodels

import androidx.compose.runtime.*
import cafe.adriel.voyager.core.model.ScreenModel
import com.trivaris.votechain.models.datastore.DataStoreRepository
import com.trivaris.votechain.models.datastore.PreferenceKeys
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class SettingsViewModel(
    private val dataStore: DataStoreRepository
) : ScreenModel {

    private var _darkMode = mutableStateOf(PreferenceKeys.DARK_MODE.defaultValue)
    val darkMode: State<Boolean> = _darkMode

    private var _dynamicColor = mutableStateOf(PreferenceKeys.DYNAMIC_COLOR.defaultValue)
    val dynamicColor: State<Boolean> = _dynamicColor

    private var _username = mutableStateOf(PreferenceKeys.USERNAME.defaultValue)
    val username: State<String> = _username

    init {
        CoroutineScope(Dispatchers.Main).launch {
            dataStore.readPref(PreferenceKeys.DARK_MODE)
                .collectLatest { _darkMode.value = it }
        }
        CoroutineScope(Dispatchers.Main).launch {
            dataStore.readPref(PreferenceKeys.DYNAMIC_COLOR)
                .collectLatest { _dynamicColor.value = it }
        }
        CoroutineScope(Dispatchers.Main).launch {
            dataStore.readPref(PreferenceKeys.USERNAME)
                .collectLatest { _username.value = it }
        }
    }

    fun setDarkMode(enabled: Boolean) {
        CoroutineScope(Dispatchers.Main).launch {
            dataStore.editPref(PreferenceKeys.DARK_MODE, enabled)
        }
    }

    fun setDynamicColor(enabled: Boolean) {
        CoroutineScope(Dispatchers.Main).launch {
            dataStore.editPref(PreferenceKeys.DYNAMIC_COLOR, enabled)
        }
    }

    fun setUsername(usernameNew: String) {
        CoroutineScope(Dispatchers.Main).launch {
            dataStore.editPref(PreferenceKeys.USERNAME, usernameNew)
        }
    }
}